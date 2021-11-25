package io.mustelidae.seaotter.domain.uploader

import io.mustelidae.seaotter.config.AppEnvironment
import io.mustelidae.seaotter.domain.delivery.Image
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.net.URL
import java.util.Locale

@Service
class UploadHandler
@Autowired constructor(
    private val appEnvironment: AppEnvironment
) {

    private val uploader = UploadTarget.valueOf(appEnvironment.uploader.uppercase(Locale.getDefault()))

    fun upload(image: Image): URL {
        image.reviseFormat()
        val uploader = getUploader()
        val pathOfImage = uploader.upload(image)
        return makeUrl(pathOfImage)
    }

    private fun getUploader(): Uploader {
        val topicCode = findTopicCode()

        return when (uploader) {
            UploadTarget.AWS_S3 -> {
                AwsS3Uploader(appEnvironment.awsS3, topicCode)
            }
            UploadTarget.LOCAL -> {
                LocalStorageUploader(appEnvironment.localStorage, topicCode)
            }
            UploadTarget.AZURE -> {
                AzureStorageUploader(appEnvironment.azureStorage, topicCode)
            }
        }
    }
    private fun makeUrl(pathOfImage: String): URL {
        return when (uploader) {
            UploadTarget.AWS_S3 -> {
                AwsS3Uploader.makeUrl(appEnvironment.awsS3, pathOfImage)
            }
            UploadTarget.LOCAL -> {
                LocalStorageUploader.makeUrl(appEnvironment.localStorage, pathOfImage)
            }
            UploadTarget.AZURE -> URL(pathOfImage)
        }
    }

    /**
     * use magic.
     */
    private fun findTopicCode(): String? {
        if (RequestContextHolder.getRequestAttributes() == null)
            return null

        return (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes)
            .request.getParameter("topic")
    }
}
