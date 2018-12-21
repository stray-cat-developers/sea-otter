@file:Suppress("UnstableApiUsage")

package io.mustelidae.seaotter.utils

import com.google.common.io.Files
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

fun getTestImageFileAsAbsolutePath(fileName: String): String {
    val path = File("src/test/resources/image/$fileName").absolutePath
    println("test image file path is $path")
    return path
}

fun getOutputFile(fileName: String): File {
    val file = File("out/image")
    if (file.exists().not()) {
        file.mkdir()
    }
    return File(file.absolutePath, fileName)
}

fun write(targetImage: BufferedImage, fileName: String) {
    var bufferedImage = targetImage

    if (bufferedImage.colorModel.pixelSize == 32) {
        val convert = BufferedImage(bufferedImage.width, bufferedImage.height, BufferedImage.TYPE_INT_RGB)
        convert.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null)
        bufferedImage = convert
    }

    val outputStream = ByteArrayOutputStream()
    ImageIO.write(bufferedImage, "jpg", outputStream)

    val outFile = getOutputFile("$fileName.jpg")

    println("out file path: ${outFile.absolutePath}")
    outFile.createNewFile()
    Files.write(outputStream.toByteArray(), outFile)
}
