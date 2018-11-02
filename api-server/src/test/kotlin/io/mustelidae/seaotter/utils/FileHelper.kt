package io.mustelidae.seaotter.utils

import java.io.File

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
