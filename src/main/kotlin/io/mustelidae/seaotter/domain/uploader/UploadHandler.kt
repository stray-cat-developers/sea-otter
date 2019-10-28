package io.mustelidae.seaotter.domain.uploader

import io.mustelidae.seaotter.config.AppEnvironment
import io.mustelidae.seaotter.domain.delivery.Image
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.URL

@Service
class UploadHandler
@Autowired constructor(
    private val appEnvironment: AppEnvironment
) {

    private val uploader = UploadTarget.valueOf(appEnvironment.uploader.toUpperCase())

    fun upload(image: Image): URL {
        image.reviseFormat()
        val uploader = getUploader()
        val pathOfImage = uploader.upload(image)
        return makeUrl(pathOfImage)
    }

    private fun getUploader(): Uploader {
        return when (uploader) {
            UploadTarget.S3 -> {
                S3Uploader(appEnvironment.awsS3)
            }
            UploadTarget.LOCAL -> {
                LocalStorageUploader(appEnvironment.localStorage)
            }
        }
    }
    private fun makeUrl(pathOfImage: String): URL {
        return when (uploader) {
            UploadTarget.S3 -> {
                S3Uploader.makeUrl(appEnvironment.awsS3, pathOfImage)
            }
            UploadTarget.LOCAL -> {
                LocalStorageUploader.makeUrl(appEnvironment.localStorage, pathOfImage)
            }
        }
    }
}
