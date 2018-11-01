package io.mustelidae.seaotter.domain.image

import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.geometry.Positions
import net.coobird.thumbnailator.resizers.configurations.Antialiasing
import net.coobird.thumbnailator.resizers.configurations.ScalingMode
import java.awt.image.BufferedImage


class ThumbnailatorFlabbyImage(
        private var bufferedImage: BufferedImage,
        private val antiAliasing: Antialiasing = Antialiasing.ON
): FlabbyImage {

    override fun resize(width: Int, height: Int) {
        val scalingMode = choiceScalingMode(width, height)
        bufferedImage = Thumbnails.of(bufferedImage)
                .antialiasing(Antialiasing.ON)
                .width(width)
                .height(height)
                .scalingMode(scalingMode)
                .asBufferedImage()
    }

    override fun resize(scale: Double) {
        val scalingMode = choiceScalingMode(scale)
        bufferedImage = Thumbnails.of(bufferedImage)
                .antialiasing(antiAliasing)
                .scale(scale * 0.01)
                .scalingMode(scalingMode)
                .asBufferedImage()
    }

    /**
     * crop by the center
     */
    override fun crop(width: Int, height: Int) {
        bufferedImage = Thumbnails.of(bufferedImage)
                .antialiasing(antiAliasing)
                .size(width,height)
                .crop(Positions.CENTER)
                .asBufferedImage()
    }

    /**
     * crop by the center
     */
    override fun crop(x1: Int, y1: Int, x2: Int, y2: Int) {
        val width = Math.abs(x1 - x2)
        val height = Math.abs(y1 - y2)

        bufferedImage = Thumbnails.of(bufferedImage)
                .antialiasing(antiAliasing)
                .size(width,height)
                .crop(Positions.CENTER)
                .asBufferedImage()
    }

    override fun getBufferedImage(): BufferedImage = bufferedImage

    private fun choiceScalingMode(scale: Double): ScalingMode {
        return if(scale > 100.0) ScalingMode.BICUBIC else ScalingMode.BILINEAR
    }

    private fun choiceScalingMode(width: Int, height: Int): ScalingMode {
        return if(this.getBufferedImage().width < width && this.getBufferedImage().height < height) ScalingMode.BICUBIC else ScalingMode.BILINEAR
    }
}
