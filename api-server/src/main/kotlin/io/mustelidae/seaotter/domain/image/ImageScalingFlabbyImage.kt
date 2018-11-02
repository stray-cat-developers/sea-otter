package io.mustelidae.seaotter.domain.image

import com.mortennobel.imagescaling.AdvancedResizeOp
import com.mortennobel.imagescaling.ResampleFilter
import com.mortennobel.imagescaling.ResampleFilters
import com.mortennobel.imagescaling.ResampleOp
import java.awt.AlphaComposite
import java.awt.RenderingHints
import java.awt.image.BufferedImage


class ImageScalingFlabbyImage(
        private var bufferedImage: BufferedImage
): FlabbyImage {

    /**
     * Ignore the proportions of the image and resize it.
     */
    override fun resize(width: Int, height: Int) {
        if(isSameSize(width, height)) return

        val rescaleOp = ResampleOp(width, height)
        rescaleOp.unsharpenMask = AdvancedResizeOp.UnsharpenMask.Normal
        rescaleOp.filter = choiceFilter(width, height)
        bufferedImage = rescaleOp.filter(bufferedImage, null)
    }

    /**
     * Resize the image.
     */
    override fun resize(scale: Double) {
        val (width, height) = ratioToPixelSize(scale, bufferedImage.width, bufferedImage.height)
        val rescaleOp = ResampleOp(width,height)
        rescaleOp.unsharpenMask = AdvancedResizeOp.UnsharpenMask.Normal
        rescaleOp.filter = choiceFilter(scale)
        bufferedImage = rescaleOp.filter(bufferedImage, null)
    }

    /**
     * Crop an image based on center.
     */
    override fun crop(width: Int, height: Int) {
        if(bufferedImage.width < width || bufferedImage.height < height)
            throw IllegalArgumentException("invalid crop coordinates")

        if(isSameSize(width,height))
            return

        val centerOfX = bufferedImage.width / 2
        val centerOfY = bufferedImage.height / 2

        val x1 = centerOfX - (width / 2)
        val y1 = centerOfY - (height / 2)
        val x2 = x1 + width
        val y2 = y1 + height

        crop(x1, y1, x2, y2)
    }

    /**
     * Crop using the coordinates.
     */
    override fun crop(x1: Int, y1: Int, x2: Int, y2: Int) {
        if (x1 < 0 || x2 <= x1 || y1 < 0 || y2 <= y1 || x2 > bufferedImage.width || y2 > bufferedImage.height)
            throw IllegalArgumentException("invalid crop coordinates")

        val width = x2 - x1
        val height = y2 - y1

        val cropBufferedImage = BufferedImage(width,height, BufferedImage.TYPE_INT_RGB)
        val graphics = cropBufferedImage.createGraphics()
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        graphics.composite = AlphaComposite.Src

        graphics.drawImage(bufferedImage, 0, 0, width, height, x1, y1, x2, y2, null)
        graphics.dispose()

        bufferedImage = cropBufferedImage
    }

    override fun getBufferedImage(): BufferedImage = bufferedImage

    private fun choiceFilter(scale: Double): ResampleFilter {
        return if(scale > 100.0) ResampleFilters.getMitchellFilter() else ResampleFilters.getLanczos3Filter()
    }

    private fun choiceFilter(width: Int, height: Int): ResampleFilter {
        return if(this.getBufferedImage().width < width && this.getBufferedImage().height < height)
            ResampleFilters.getMitchellFilter()
        else
            ResampleFilters.getLanczos3Filter()
    }
}
