package io.mustelidae.seaotter.domain.image

import org.junit.jupiter.api.Test
import java.awt.image.BufferedImage


class ImageScalingPerformanceTest: ImagePerformanceTemplate() {

    override fun resize(bufferedImage: BufferedImage, scale: Double): BufferedImage {
        val flabbyImage = ImageScalingFlabbyImage(bufferedImage)
        flabbyImage.resize(scale)
        return flabbyImage.getBufferedImage()
    }

    override fun getProcessorName(): String = "imageScaling"

    @Test
    fun resizePerformance(){
        resizeProcess()
    }
}
