package io.mustelidae.seaotter.domain.editor.image

import org.junit.jupiter.api.Test
import java.awt.image.BufferedImage

class ImgscalrPerformanceTest : ImagePerformanceTemplate() {
    override fun crop(bufferedImage: BufferedImage, x1: Int, y1: Int, x2: Int, y2: Int): BufferedImage {
        val flabbyImage = ImgscalrFlabbyImage(bufferedImage)
        flabbyImage.crop(x1, y1, x2, y2)
        return flabbyImage.getBufferedImage()
    }

    override fun crop(bufferedImage: BufferedImage, width: Int, height: Int): BufferedImage {
        val imgscalrFlabbyImage = ImgscalrFlabbyImage(bufferedImage)
        imgscalrFlabbyImage.crop(width, height)
        return imgscalrFlabbyImage.getBufferedImage()
    }

    override fun resize(bufferedImage: BufferedImage, width: Int, height: Int): BufferedImage {
        val imgscalrFlabbyImage = ImgscalrFlabbyImage(bufferedImage)
        imgscalrFlabbyImage.resize(width, height)
        return imgscalrFlabbyImage.getBufferedImage()
    }

    override fun getProcessorName(): String = "imgscalr"

    override fun resize(bufferedImage: BufferedImage, scale: Double): BufferedImage {
        val imgscalrFlabbyImage = ImgscalrFlabbyImage(bufferedImage)
        imgscalrFlabbyImage.resize(scale)
        return imgscalrFlabbyImage.getBufferedImage()
    }

    @Test
    fun resizePerformance() {
        allImageAreScaledUsingProportions()
    }

    @Test
    fun imageFixFrameTest() {
        allImageAreFrameUsingProportions()
    }

    @Test
    fun imageCropTest() {
        cropAllImagesToFixedSize()
    }

    @Test
    fun imageCropTestByCoordinate() {
        cropAllImagesToCoordinate()
    }
}
