package io.mustelidae.seaotter.domain.image

import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.geometry.Positions
import net.coobird.thumbnailator.resizers.configurations.AlphaInterpolation
import net.coobird.thumbnailator.resizers.configurations.Antialiasing
import net.coobird.thumbnailator.resizers.configurations.ScalingMode
import java.awt.image.BufferedImage

class ThumbnailatorFlabbyImage(
    private var bufferedImage: BufferedImage,
    private val antiAliasing: Antialiasing = Antialiasing.ON
) : FlabbyImage {

    /**
     * Resize the image while maintaining the ratio of the image based on the width.
     */
    override fun resize(width: Int, height: Int) {
        val scalingMode = choiceScalingMode(width, height)
        bufferedImage = Thumbnails.of(bufferedImage)
                .antialiasing(Antialiasing.ON)
                .width(width)
                .height(height)
                .scalingMode(scalingMode)
                .asBufferedImage()
    }

    /**
     * Resize the image.
     */
    override fun resize(scale: Double) {
        val scalingMode = choiceScalingMode(scale)
        bufferedImage = Thumbnails.of(bufferedImage)
                .antialiasing(antiAliasing)
                .scale(scale * 0.01)
                .scalingMode(scalingMode)
                .asBufferedImage()
    }

    /**
     * Resize the size of the image to be cropped.
     * Then crop the input size.
     * At this time, the position to crop is centered.
     */
    override fun crop(width: Int, height: Int) {
        bufferedImage = Thumbnails.of(bufferedImage)
                .antialiasing(Antialiasing.OFF)
                .size(width, height)
                .crop(Positions.CENTER)
                .asBufferedImage()
    }

    /**
     * Converts the coordinates to the size of the image.
     * The image is then resized and cropped.
     * The image size is created by the size of two coordinates. However, it does not crop specific areas of the original image.
     * A thumbnail is created with the size of the coordinates, and then cropped to compensate.
     */
    override fun crop(x1: Int, y1: Int, x2: Int, y2: Int) {
        val width = Math.abs(x1 - x2)
        val height = Math.abs(y1 - y2)

        bufferedImage = Thumbnails.of(bufferedImage)
                .antialiasing(Antialiasing.OFF)
                .alphaInterpolation(AlphaInterpolation.QUALITY)
                .crop(Positions.CENTER)
                .size(width, height)
                .keepAspectRatio(true)
                .asBufferedImage()
    }

    override fun getBufferedImage(): BufferedImage = bufferedImage

    private fun choiceScalingMode(scale: Double): ScalingMode {
        return if (scale > 100.0) ScalingMode.BICUBIC else ScalingMode.BILINEAR
    }

    private fun choiceScalingMode(width: Int, height: Int): ScalingMode {
        return if (this.getBufferedImage().width < width && this.getBufferedImage().height < height) ScalingMode.BICUBIC else ScalingMode.BILINEAR
    }
}
