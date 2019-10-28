package io.mustelidae.seaotter.domain.editor.image

import org.imgscalr.Scalr
import java.awt.image.BufferedImage

class ImgscalrFlabbyImage(
    private var bufferedImage: BufferedImage
) : FlabbyImage {

    /**
     * Resize the image while maintaining the ratio of the image based on the width.
     */
    override fun resize(width: Int, height: Int) {
        bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, width)
    }

    /**
     * Resize the image.
     */
    override fun resize(scale: Double) {
        val fixedScale = ratioToPixelSize(scale, bufferedImage.width, bufferedImage.height)
        bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, fixedScale.first)
    }

    /**
     * Crop based on the upper left corner of the image.
     */
    override fun crop(width: Int, height: Int) {
        bufferedImage = Scalr.crop(bufferedImage, width, height)
    }

    /**
     * Crop using the coordinates.
     * The coordinates are based on the upper left corner of the image.
     * Both coordinates use positive integers.
     * Both coordinates must be entered as positive integers, and y1, y2 are calculated automatically by changing to a negative number.
     */
    override fun crop(x1: Int, y1: Int, x2: Int, y2: Int) {
        val width = x2 - x1
        val height = y2 - y1

        bufferedImage = Scalr.crop(bufferedImage, x1, y1, width, height)
    }

    fun cropByPointScale(x1: Int, y1: Int, width: Int, height: Int) {
        bufferedImage = Scalr.crop(bufferedImage, x1, y1, width, height)
    }

    /**
     * Rotate the image.
     * It can only be right angled or reversed.
     */
    fun rotate(rotation: Scalr.Rotation) {
        bufferedImage = Scalr.rotate(bufferedImage, rotation)
    }

    override fun getBufferedImage(): BufferedImage = this.bufferedImage
}
