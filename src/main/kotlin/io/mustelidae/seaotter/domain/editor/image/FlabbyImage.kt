package io.mustelidae.seaotter.domain.editor.image

import io.mustelidae.seaotter.constant.ImageFileFormat
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.math.roundToInt

interface FlabbyImage {

    @Throws(IOException::class)
    fun resize(width: Int, height: Int)

    @Throws(IOException::class)
    fun resize(scale: Double)

    @Throws(IOException::class)
    fun crop(width: Int, height: Int)

    @Throws(IOException::class)
    fun crop(x1: Int, y1: Int, x2: Int, y2: Int)

    fun getBufferedImage(): BufferedImage

    fun ratioToPixelSize(ratio: Double, width: Int, height: Int): Pair<Int, Int> {
        if (ratio == 0.0 || ratio == 100.0)
            return Pair(width, height)

        val fixedWidth = (width * (ratio * 0.01)).roundToInt()
        val fixedHeight = (height * (ratio * 0.01)).roundToInt()

        return Pair(fixedWidth, fixedHeight)
    }

    fun isSameSize(width: Int, height: Int): Boolean {
        return (getBufferedImage().width == width && getBufferedImage().height == height)
    }

    companion object {
        fun getBufferedImage(byte: ByteArray): BufferedImage {

            return ImageIO.read(ByteArrayInputStream(byte))
        }

        private fun isSupportFormat(extension: String): Boolean =
                ImageFileFormat.valueOf(extension.toUpperCase()).support
    }
}
