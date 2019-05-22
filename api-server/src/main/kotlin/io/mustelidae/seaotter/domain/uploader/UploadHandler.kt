package io.mustelidae.seaotter.domain.uploader

import io.mustelidae.seaotter.config.OtterEnvironment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UploadHandler
@Autowired constructor(
    private val otterEnvironment: OtterEnvironment
) {

    private val uploader = UploadTarget.valueOf(otterEnvironment.uploader.toUpperCase())

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
                S3Uploader(otterEnvironment.awsS3)
            }
            UploadTarget.LOCAL -> {
                LocalStorageUploader(otterEnvironment.localStorage)
            }
        }
    }

    private fun getSavePath(isOriginal: Boolean): String {
        return when (uploader) {
            UploadTarget.S3 -> {
                if (isOriginal)
                    otterEnvironment.awsS3.path.unRetouchedPath
                else
                    otterEnvironment.awsS3.path.editedPath
            }
            UploadTarget.LOCAL -> {
                if (isOriginal)
                    otterEnvironment.localStorage.path.unRetouchedPath
                else
                    otterEnvironment.localStorage.path.editedPath
            }
        }
    }
}
