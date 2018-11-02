package io.mustelidae.seaotter.domain.image

import org.junit.jupiter.api.Test
import java.awt.image.BufferedImage


class ThumbnailatorPerformanceTest: ImagePerformanceTemplate() {
    override fun crop(bufferedImage: BufferedImage, width: Int, height: Int): BufferedImage {
        val flabbyImage = ThumbnailatorFlabbyImage(bufferedImage)
        flabbyImage.crop(width, height)
        return flabbyImage.getBufferedImage()
    }

    override fun resize(bufferedImage: BufferedImage, width: Int, height: Int): BufferedImage {
        val flabbyImage = ThumbnailatorFlabbyImage(bufferedImage)
        flabbyImage.resize(width, height)
        return flabbyImage.getBufferedImage()
    }

    override fun resize(bufferedImage: BufferedImage, scale: Double): BufferedImage {
        val flabbyImage = ThumbnailatorFlabbyImage(bufferedImage)
        flabbyImage.resize(scale)
        return flabbyImage.getBufferedImage()
    }

    override fun getProcessorName(): String = "thumbnailator"

    @Test
    fun resizePerformance(){
        allImageAreScaledUsingProportions()
    }

    @Test
    fun imageFixFrameTest(){
        allImageAreFrameUsingProportions()
    }

    @Test
    fun imageCropTest() {
        cropAllImagesToFixedSize()
    }
}
