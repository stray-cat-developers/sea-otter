package io.mustelidae.seaotter.domain.uploader

import java.awt.image.BufferedImage

interface Uploader {

    fun upload(bytes: ByteArray): String

    fun upload(bufferedImage: BufferedImage): String
}
