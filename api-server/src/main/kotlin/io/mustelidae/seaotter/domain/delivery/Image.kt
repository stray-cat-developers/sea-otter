package io.mustelidae.seaotter.domain.delivery

import io.mustelidae.seaotter.config.UnSupportException
import io.mustelidae.seaotter.constant.ImageFileFormat
import io.mustelidae.seaotter.utils.extension
import io.mustelidae.seaotter.utils.isSupport
import org.bson.types.ObjectId
import org.springframework.util.Base64Utils
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

    fun randomizeName() {
        name = ObjectId().toString()
    }

    companion object {
        private val base64Regex = Regex("(data:image/)(.*);base64")

        fun from(multipartFile: MultipartFile): Image {
            if (multipartFile.isSupport().not())
                throw UnSupportException()

            return Image(
                ImageIO.read(ByteArrayInputStream(multipartFile.bytes)) ?: throw IllegalArgumentException("No images uploaded."),
                multipartFile.name ?: ObjectId().toString(),
                multipartFile.extension(),
                true
            )
        }

        fun from(file: File): Image {
            if (file.isFile.not())
                throw java.lang.IllegalArgumentException("Invalid image file path")

            return Image(
                ImageIO.read(file),
                file.nameWithoutExtension,
                ImageFileFormat.valueOf(file.extension.toUpperCase()),
                true
            )
        }

        fun from(base64: String): Image {
            val index = base64.lastIndexOf(',')
            val byteArray = Base64Utils.decodeFromString(base64.substring(index + 1))
            val bufferedImage = ImageIO.read(ByteArrayInputStream(byteArray))
            val format: ImageFileFormat

            try {
                val extension = base64Regex.find(base64.substring(0, index))!!.groupValues[2]
                format = ImageFileFormat.valueOf(extension.toUpperCase())
            } catch (e: NullPointerException) {
                throw java.lang.IllegalArgumentException("invalid base 64 image format")
            } catch (e: java.lang.IllegalArgumentException) {
                throw UnSupportException()
            }

            if (format.support.not())
                throw UnSupportException()

            return Image(
                bufferedImage,
                ObjectId().toString(),
                format,
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
