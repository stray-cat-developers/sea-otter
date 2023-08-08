package io.mustelidae.seaotter.domain.delivery

import io.mustelidae.seaotter.config.UnSupportException
import io.mustelidae.seaotter.constant.ImageFileFormat
import io.mustelidae.seaotter.utils.extension
import io.mustelidae.seaotter.utils.isSupport
import org.bson.types.ObjectId
import org.springframework.util.Base64Utils
import org.springframework.web.multipart.MultipartFile
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.net.URL
import java.util.Locale
import javax.imageio.ImageIO

data class Image(
    var bufferedImage: BufferedImage,
    var name: String,
    var imageFileFormat: ImageFileFormat,
    val isOriginal: Boolean
) {

    fun randomizeName() {
        name = ObjectId().toString()
    }

    fun reviseFormat() {
        if (isOriginal.not() && bufferedImage.colorModel.pixelSize == 32) {
            val convert = BufferedImage(bufferedImage.width, bufferedImage.height, BufferedImage.TYPE_INT_RGB)
            convert.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null)
            bufferedImage = convert
            imageFileFormat = ImageFileFormat.JPG
        }
    }

    companion object {
        private val base64Regex = Regex("(data:image/)(.*);base64")
        private val extensionRegex = Regex("(?<=/)[^/?#]+(?=[^/]*\$)")

        fun from(multipartFile: MultipartFile): Image {
            if (multipartFile.isSupport().not())
                throw UnSupportException()

            return Image(
                ImageIO.read(ByteArrayInputStream(multipartFile.bytes)) ?: throw IllegalArgumentException("No images uploaded."),
                multipartFile.name,
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
                ImageFileFormat.valueOf(file.extension.uppercase(Locale.getDefault())),
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
                format = ImageFileFormat.valueOf(extension.uppercase(Locale.getDefault()))
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

        fun from(url: URL): Image {
            val file = extensionRegex.find(url.path)!!.groupValues[0]
            val name = file.substringBeforeLast(".")
            val extension = file.substringAfterLast('.', "")
            return Image(
                ImageIO.read(url),
                name,
                ImageFileFormat.valueOf(extension.uppercase()),
                true
            )
        }
    }

    fun getExtension(): String {
        return imageFileFormat.name.lowercase(Locale.getDefault())
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
