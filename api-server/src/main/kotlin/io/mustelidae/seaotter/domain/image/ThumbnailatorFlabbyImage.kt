package io.mustelidae.seaotter.domain.image

import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.geometry.Positions
import net.coobird.thumbnailator.resizers.configurations.AlphaInterpolation
import net.coobird.thumbnailator.resizers.configurations.Antialiasing
import net.coobird.thumbnailator.resizers.configurations.ScalingMode
import java.awt.image.BufferedImage

class ThumbnailatorFlabbyImage(
    private val bufferedImage: BufferedImage,
    antiAliasing: Antialiasing = Antialiasing.ON
) : FlabbyImage {

    private val thumbnailsBuilder: Thumbnails.Builder<BufferedImage> = Thumbnails.of(bufferedImage).antialiasing(antiAliasing)

    /**
     * Resize the image while maintaining the ratio of the image based on the width.
     */
    override fun resize(width: Int, height: Int) {
        val scalingMode = choiceScalingMode(width, height)
        thumbnailsBuilder
                .width(width)
                .height(height)
                .scalingMode(scalingMode)
    }

    /**
     * Resize the image.
     */
    override fun resize(scale: Double) {
        val scalingMode = choiceScalingMode(scale)
        thumbnailsBuilder
                .scale(scale * 0.01)
                .scalingMode(scalingMode)
    }

    /**
     * Resize the size of the image to be cropped.
     * Then crop the input size.
     * At this time, the position to crop is centered.
     */
    override fun crop(width: Int, height: Int) {
        thumbnailsBuilder
                .size(width, height)
                .crop(Positions.CENTER)
    }

    fun rotate(angle: Double) {
        thumbnailsBuilder
                .rotate(angle)
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

        thumbnailsBuilder
                .alphaInterpolation(AlphaInterpolation.QUALITY)
                .crop(Positions.CENTER)
                .size(width, height)
                .keepAspectRatio(true)
    }

    override fun getBufferedImage(): BufferedImage = thumbnailsBuilder.asBufferedImage()

    private fun choiceScalingMode(scale: Double): ScalingMode {
        return if (scale > 100.0) ScalingMode.BICUBIC else ScalingMode.BILINEAR
    }

    private fun choiceScalingMode(width: Int, height: Int): ScalingMode {
        return if (bufferedImage.width < width && bufferedImage.height < height) ScalingMode.BICUBIC else ScalingMode.BILINEAR
    }
}
