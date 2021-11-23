package io.mustelidae.seaotter.domain.editor.image

import io.mustelidae.seaotter.domain.delivery.Image
import io.mustelidae.seaotter.utils.getTestImageFileAsAbsolutePath
import io.mustelidae.seaotter.utils.write
import java.awt.image.BufferedImage
import java.io.File
import kotlin.system.measureTimeMillis

abstract class ImagePerformanceTemplate {

    val imageFiles = listOf(
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

    abstract fun resize(bufferedImage: BufferedImage, scale: Double): BufferedImage
    abstract fun resize(bufferedImage: BufferedImage, width: Int, height: Int): BufferedImage
    abstract fun crop(bufferedImage: BufferedImage, width: Int, height: Int): BufferedImage
    abstract fun crop(bufferedImage: BufferedImage, x1: Int, y1: Int, x2: Int, y2: Int): BufferedImage

    abstract fun getProcessorName(): String

    fun allImageAreScaledUsingProportions() {
        val scale = 40.0
        for (imageFileName in imageFiles) {
            resizeUsingScale(imageFileName, scale)
        }
    }

    fun allImageAreFrameUsingProportions() {
        val width = 400
        val height = 350
        for (imageFileName in imageFiles) {
            resizeUsingFixFrame(imageFileName, width, height)
        }
    }

    fun cropAllImagesToFixedSize() {
        val width = 500
        val height = 500

        for (imageFileName in imageFiles) {
            cropTheImage(imageFileName, width, height)
        }
    }

    fun cropAllImagesToCoordinate() {
        val x1 = 150
        val y1 = 150
        val x2 = 400
        val y2 = 400

        for (imageFileName in imageFiles) {
            cropTheImage(imageFileName, x1, y1, x2, y2)
        }
    }

    private fun cropTheImage(fileName: String, x1: Int, y1: Int, x2: Int, y2: Int) {
        val inputPath = getTestImageFileAsAbsolutePath(fileName)
        var bufferedImage = Image.from(File(inputPath)).bufferedImage

        val time = measureTimeMillis {
            bufferedImage = crop(bufferedImage, x1, y1, x2, y2)
        }
        println("crop the image time: $time")

        write(bufferedImage, "${fileName}_to_crop_${getProcessorName()}")
    }

    private fun cropTheImage(fileName: String, width: Int, height: Int) {
        val inputPath = getTestImageFileAsAbsolutePath(fileName)
        var bufferedImage = Image.from(File(inputPath)).bufferedImage

        val time = measureTimeMillis {
            bufferedImage = crop(bufferedImage, width, height)
        }
        println("crop the image time: $time")

        write(bufferedImage, "${fileName}_to_crop_${getProcessorName()}")
    }

    private fun resizeUsingScale(fileName: String, scale: Double) {
        val inputPath = getTestImageFileAsAbsolutePath(fileName)
        var bufferedImage = Image.from(File(inputPath)).bufferedImage

        val time = measureTimeMillis {
            bufferedImage = resize(bufferedImage, scale)
        }
        println("resize process time: $time")

        write(bufferedImage, "${fileName}_to_scale_resize_${getProcessorName()}")
    }

    private fun resizeUsingFixFrame(fileName: String, width: Int, height: Int) {
        val inputPath = getTestImageFileAsAbsolutePath(fileName)
        var bufferedImage = Image.from(File(inputPath)).bufferedImage

        val time = measureTimeMillis {
            bufferedImage = resize(bufferedImage, width, height)
        }
        println("resize process time: $time")

        write(bufferedImage, "${fileName}_to_frame_resize_${getProcessorName()}")
    }
}
