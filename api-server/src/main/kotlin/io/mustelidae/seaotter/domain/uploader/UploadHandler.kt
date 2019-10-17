package io.mustelidae.seaotter.domain.uploader

import io.mustelidae.seaotter.config.AppEnvironment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UploadHandler
@Autowired constructor(
    private val appEnvironment: AppEnvironment
) {

    private val uploader = UploadTarget.valueOf(appEnvironment.uploader.toUpperCase())

    fun upload(uploadFile: UploadFile): String {
        val uploader = getUploader().apply {
            initPath(getSavePath(false))
            initFile(uploadFile.fileFormat, uploadFile.name)
        }
        return uploader.upload(uploadFile.bufferedImage)
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

    private fun getSavePath(isOriginal: Boolean): String {
        return when (uploader) {
            UploadTarget.S3 -> {
                if (isOriginal)
                    appEnvironment.awsS3.path.unRetouchedPath
                else
                    appEnvironment.awsS3.path.editedPath
            }
            UploadTarget.LOCAL -> {
                if (isOriginal)
                    appEnvironment.localStorage.path.unRetouchedPath
                else
                    appEnvironment.localStorage.path.editedPath
            }
        }
    }
}
