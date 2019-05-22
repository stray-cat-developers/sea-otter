package io.mustelidae.seaotter.domain.uploader

import com.google.common.io.Files
import io.mustelidae.seaotter.config.OtterEnvironment
import io.mustelidae.seaotter.constant.ImageFileFormat
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Paths
import javax.imageio.ImageIO

internal class LocalStorageUploader(
    private val localStorage: OtterEnvironment.LocalStorage
) : Uploader {

    private lateinit var prefixPath: String
    private lateinit var imageFileFormat: ImageFileFormat
    private lateinit var fileName: String

    override fun initFile(imageFileFormat: ImageFileFormat, name: String) {
        this.imageFileFormat = imageFileFormat
        this.fileName = "$name.${imageFileFormat.name.toLowerCase()}"
    }

    override fun initPath(path: String) {
        prefixPath = path
    }

    override fun upload(bytes: ByteArray): String {
        val basePath = getPath(prefixPath)
        val path = File(basePath)
        if (path.exists().not())
            path.mkdirs()

        val pathAndFileName = "$basePath/$fileName"
        val file = File(pathAndFileName)

        file.createNewFile()
        Files.write(bytes, file)

        return pathAndFileName
    }

    override fun upload(bufferedImage: BufferedImage): String {
        val out = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, imageFileFormat.name.toLowerCase(), out)
        return upload(out.toByteArray())
    }

    override fun makeFullUrl(pathAndFileName: String): String {
        return Paths.get(localStorage.url, pathAndFileName)
                .toAbsolutePath()
                .toString()
    }

    private fun getPath(prefixPath: String): String {
        return when (localStorage.shardType) {
            "date" -> PathGenerator.getPathByDate(prefixPath)
            else -> ""
        }
    }
}
