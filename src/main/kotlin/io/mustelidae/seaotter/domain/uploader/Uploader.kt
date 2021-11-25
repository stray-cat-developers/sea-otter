package io.mustelidae.seaotter.domain.uploader

import io.mustelidae.seaotter.domain.delivery.Image

interface Uploader {
    /* topic code (objectId) */
    val topicCode: String?
    fun upload(image: Image): String
}

enum class UploadTarget {
    AWS_S3, LOCAL, AZURE
}
