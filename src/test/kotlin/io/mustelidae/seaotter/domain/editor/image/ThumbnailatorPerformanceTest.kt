package io.mustelidae.seaotter.domain.editor.image

import io.mustelidae.seaotter.domain.delivery.Image
import io.mustelidae.seaotter.utils.getTestImageFileAsAbsolutePath
import io.mustelidae.seaotter.utils.write
import org.junit.jupiter.api.Test
import java.awt.image.BufferedImage
import java.io.File

class ThumbnailatorPerformanceTest : ImagePerformanceTemplate() {

    override fun crop(bufferedImage: BufferedImage, x1: Int, y1: Int, x2: Int, y2: Int): BufferedImage {
        val flabbyImage = ThumbnailatorFlabbyImage(bufferedImage)
        flabbyImage.crop(x1, y1, x2, y2)
        return flabbyImage.getBufferedImage()
    }

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

    @Test
    fun compressTest() {
        val fileName = super.imageFiles.first()
        val inputPath = getTestImageFileAsAbsolutePath(fileName)
        val image = Image.from(File(inputPath))

        val flabbyImage = ThumbnailatorFlabbyImage(image.bufferedImage)

        flabbyImage.compress(0.9)
        val compressedBufferedImage = flabbyImage.getBufferedImage()

        write(compressedBufferedImage, "${fileName}_to_compress_${getProcessorName()}")
    }
}
