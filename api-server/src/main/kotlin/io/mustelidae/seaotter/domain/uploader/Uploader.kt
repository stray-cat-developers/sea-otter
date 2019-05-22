package io.mustelidae.seaotter.domain.uploader

import io.mustelidae.seaotter.constant.ImageFileFormat
import java.awt.image.BufferedImage

interface Uploader {

    fun initFile(imageFileFormat: ImageFileFormat, name: String)

    fun initPath(path: String)

    fun upload(bytes: ByteArray): String

    fun upload(bufferedImage: BufferedImage): String

    fun makeFullUrl(pathAndFileName: String): String
}

enum class UploadTarget {
    S3, LOCAL
}
