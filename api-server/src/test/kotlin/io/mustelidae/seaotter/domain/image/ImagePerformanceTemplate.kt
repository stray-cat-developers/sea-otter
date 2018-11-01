package io.mustelidae.seaotter.domain.image

import com.google.common.io.Files
import io.mustelidae.seaotter.utils.getOutputFile
import io.mustelidae.seaotter.utils.getTestImageFileAsAbsolutePath
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.system.measureTimeMillis


abstract class ImagePerformanceTemplate {

    private val imageFiles = listOf(
            "denden_CMYK_2784x1856.jpg",
            "denden_RGB_2784x1856.jpg",
            "night_RGB_2784x1856.jpg",
            "scenery_RGB_4128×2322.jpg",
            "weed_RGB_2784x1856.jpg",
            "denden_RGB_2784x1856.png",
            "way_RGB_1568x1044.png",
            "lena_RGB_512x512.tiff",
            "lena_RGB_512x512.bmp"
    )

    abstract fun resize(bufferedImage:BufferedImage, scale:Double): BufferedImage
    abstract fun getProcessorName(): String

    fun resizeProcess() {
        for(imageFileName in imageFiles){
            resize(imageFileName)
        }
    }

    private fun resize(fileName: String) {
        val inputPath = getTestImageFileAsAbsolutePath(fileName)
        val scale = 40.0

        var bufferedImage = FlabbyImage.getBufferedImage(inputPath)
        val time = measureTimeMillis {
            bufferedImage = resize(bufferedImage, scale)
        }
        println("resize process time: $time")

        val outputStream = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "jpg", outputStream)

        val outFile = getOutputFile("${fileName}_to_resize_${getProcessorName()}.jpg")

        println("out file path: ${outFile.absolutePath}")
        outFile.createNewFile()
        Files.write(outputStream.toByteArray(), outFile)
    }
}
