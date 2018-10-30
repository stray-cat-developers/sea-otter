package io.mustelidae.seaotter.domain.image

import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.resizers.configurations.Antialiasing
import net.coobird.thumbnailator.resizers.configurations.ScalingMode
import java.awt.image.BufferedImage


class ThumbnailatorFlabbyImage(
        private var bufferedImage: BufferedImage
): FlabbyImage {

    override fun resize(width: Int, height: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resize(scale: Double) {
        val scalingMode = if(isUpscale(scale)) ScalingMode.BICUBIC else ScalingMode.BILINEAR
        bufferedImage = Thumbnails.of(bufferedImage)
                .antialiasing(Antialiasing.ON)
                .scale(scale * 0.01)
                .scalingMode(scalingMode)
                .outputQuality(0.9)
                .asBufferedImage()
    }

    override fun compress(quality: Double) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun crop(width: Int, height: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun crop(x1: Int, y1: Int, x2: Int, y2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBufferedImage(): BufferedImage = bufferedImage

    private fun isUpscale(scale: Double): Boolean = (scale > 100.0)

    private fun isUpscale(width: Int, height: Int): Boolean = (bufferedImage.width < width && bufferedImage.height < height)
}
