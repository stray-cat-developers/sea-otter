package io.mustelidae.seaotter.domain.uploader

import io.mustelidae.seaotter.domain.delivery.Image

data class UploadFile(
    val image: Image,
    val isOriginal: Boolean
)
