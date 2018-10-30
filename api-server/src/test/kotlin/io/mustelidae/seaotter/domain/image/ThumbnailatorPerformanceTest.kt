package io.mustelidae.seaotter.domain.image

import org.junit.jupiter.api.Test
import java.awt.image.BufferedImage


class ThumbnailatorPerformanceTest: ImagePerformanceTemplate() {


    override fun resize(bufferedImage: BufferedImage, scale: Double): BufferedImage {
        val flabbyImage = ThumbnailatorFlabbyImage(bufferedImage)
        flabbyImage.resize(scale)
        return flabbyImage.getBufferedImage()
    }

    override fun getProcessorName(): String = "thumbnailator"

    @Test
    fun resizePerformance(){
        resizeProcess()
    }
}
