package io.mustelidae.seaotter.domain.image

import org.imgscalr.Scalr
import java.awt.image.BufferedImage


class ImgscalrFlabbyImage(
        private var bufferedImage: BufferedImage
): FlabbyImage {

    override fun resize(width: Int, height: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resize(scale: Double) {
        val fixedScale = ratioToPixelSize(scale,bufferedImage.width, bufferedImage.height)
        bufferedImage = Scalr.resize(bufferedImage,Scalr.Method.BALANCED, Scalr.Mode.AUTOMATIC, fixedScale.first)
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

    override fun getBufferedImage(): BufferedImage = this.bufferedImage
}
