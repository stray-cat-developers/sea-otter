package io.mustelidae.seaotter.domain.uploader

import io.mustelidae.seaotter.domain.delivery.Image
import org.springframework.web.multipart.MultipartFile

interface Uploader {
    /* topic code (objectId) */
    val topicCode: String?
    fun upload(image: Image): String

    fun upload(multipartFile: MultipartFile): String
}

enum class UploadTarget {
    AWS_S3, LOCAL, AZURE
}
