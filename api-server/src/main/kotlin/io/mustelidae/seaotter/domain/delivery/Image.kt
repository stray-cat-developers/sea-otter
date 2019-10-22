package io.mustelidae.seaotter.domain.delivery

import io.mustelidae.seaotter.config.UnSupportException
import io.mustelidae.seaotter.constant.ImageFileFormat
import io.mustelidae.seaotter.utils.extension
import io.mustelidae.seaotter.utils.isSupport
import org.bson.types.ObjectId
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import javax.imageio.ImageIO

data class Image(
    val bufferedImage: BufferedImage,
    var name: String,
    val imageFileFormat: ImageFileFormat,
    val isOriginal: Boolean
) {
    companion object {
        fun from(multipartFile: MultipartFile): Image {
            if (multipartFile.isSupport().not())
                throw UnSupportException()

            return Image(
                ImageIO.read(ByteArrayInputStream(multipartFile.bytes)) ?: throw IllegalArgumentException("No images uploaded."),
                multipartFile.originalFilename ?: ObjectId().toString(),
                multipartFile.extension(),
                true
            )
        }

        fun from(filePath: String): Image {
            val file = File(filePath)
            if (file.isFile.not())
                throw java.lang.IllegalArgumentException("Invalid image file path")

            return Image(
                ImageIO.read(file),
                file.nameWithoutExtension,
                ImageFileFormat.valueOf(file.extension),
                true
            )
        }
    }

    fun getExtension(): String {
        return imageFileFormat.name.toLowerCase()
    }

    fun getMeta(): Meta {
        return Meta(
            bufferedImage.width,
            bufferedImage.height
        )
    }

    data class Meta(
        val width: Int,
        val height: Int
    )
}
