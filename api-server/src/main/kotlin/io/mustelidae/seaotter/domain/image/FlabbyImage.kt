package io.mustelidae.seaotter.domain.image

import com.google.common.io.Files
import org.springframework.web.multipart.commons.CommonsMultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.net.URL
import javax.imageio.ImageIO

interface FlabbyImage {

    @Throws(IOException::class)
    fun resize(width: Int, height: Int)

    @Throws(IOException::class)
    fun resize(scale: Double)

    @Throws(IOException::class)
    fun compress(quality: Double)

    @Throws(IOException::class)
    fun crop(width: Int, height: Int)

    @Throws(IOException::class)
    fun crop(x1: Int, y1: Int, x2: Int, y2: Int)

    fun getBufferedImage(): BufferedImage

    fun ratioToPixelSize(ratio:Double, width: Int, height: Int): Pair<Int, Int> {
        if(ratio == 0.0 || ratio == 100.0)
            return Pair(width, height)

        val fixedWidth = Math.round(width *(ratio * 0.01)).toInt()
        val fixedHeight = Math.round(height * (ratio * 0.01)).toInt()

        return Pair(fixedWidth, fixedHeight)
    }

    companion object {
        fun getBufferedImage(byte: ByteArray): BufferedImage {

            return ImageIO.read(ByteArrayInputStream(byte))
        }

        fun getBufferedImage(url: URL): BufferedImage {
            val imgExt = Files.getFileExtension(url.path)

            if (isSupportFormat(imgExt).not())
                throw IllegalArgumentException("Unsupported image format.")

            return ImageIO.read(url) ?: throw IllegalArgumentException("Invalid image url.")
        }

        fun getBufferedImage(multipartFile: CommonsMultipartFile): BufferedImage {
            return ImageIO.read(ByteArrayInputStream(multipartFile.bytes)) ?: throw IllegalArgumentException("No images uploaded.")
        }

        fun getBufferedImage(path:String): BufferedImage {
            val file = File(path)
            if(file.isFile.not())
                throw java.lang.IllegalArgumentException("Invalid image file path")

            return ImageIO.read(file)
        }

        private fun isSupportFormat(extension: String): Boolean {
            return when (extension.toLowerCase()) {
                "jpg", "jpeg", "png", "psd", "bmp", "pdf", "hdr" -> true
                else -> {
                    false
                }
            }
        }
    }
}