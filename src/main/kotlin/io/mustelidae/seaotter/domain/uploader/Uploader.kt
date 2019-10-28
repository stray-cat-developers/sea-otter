package io.mustelidae.seaotter.domain.uploader

import io.mustelidae.seaotter.domain.delivery.Image

interface Uploader {
    fun upload(image: Image): String
}

enum class UploadTarget {
    S3, LOCAL
}
