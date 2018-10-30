package io.mustelidae.seaotter.domain.image

import org.junit.jupiter.api.Test
import java.awt.image.BufferedImage


class ImgscalrPerformanceTest: ImagePerformanceTemplate() {
    override fun getProcessorName(): String = "imgscalr"

    override fun resize(bufferedImage: BufferedImage, scale: Double): BufferedImage {
        val imgscalrFlabbyImage = ImgscalrFlabbyImage(bufferedImage)
        imgscalrFlabbyImage.resize(scale)
        return imgscalrFlabbyImage.getBufferedImage()
    }

    @Test
    fun resizePerformance(){
        resizeProcess()
    }
}
