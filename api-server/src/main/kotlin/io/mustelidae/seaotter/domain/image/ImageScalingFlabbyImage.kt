package io.mustelidae.seaotter.domain.image

import com.mortennobel.imagescaling.AdvancedResizeOp
import com.mortennobel.imagescaling.ResampleOp
import java.awt.image.BufferedImage


class ImageScalingFlabbyImage(
        private var bufferedImage: BufferedImage
): FlabbyImage {

    override fun resize(width: Int, height: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resize(scale: Double) {
        val (width, height) = ratioToPixelSize(scale, bufferedImage.width, bufferedImage.height)
        val rescaleOp = ResampleOp(width,height)
        rescaleOp.unsharpenMask = AdvancedResizeOp.UnsharpenMask.Soft
        bufferedImage = rescaleOp.filter(bufferedImage, null)
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
}
