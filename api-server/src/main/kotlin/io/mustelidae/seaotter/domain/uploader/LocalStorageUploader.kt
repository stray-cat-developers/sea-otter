package io.mustelidae.seaotter.domain.uploader

import com.google.common.io.FileWriteMode
import com.google.common.io.Files
import io.mustelidae.seaotter.config.OtterEnvironment
import io.mustelidae.seaotter.constant.ImageFileFormat
import org.bson.types.ObjectId
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Paths
import javax.imageio.ImageIO

class LocalStorageUploader(
    private val localStorage: OtterEnvironment.LocalStorage
) : Uploader {

    private lateinit var prefixPath: String
    private lateinit var imageFileFormat: ImageFileFormat
    private lateinit var fileName: String

    /**
     * Define the file you want to save.
     */
    fun defineFile(imageFileFormat: ImageFileFormat, name: String = ObjectId().toString()) {
        this.imageFileFormat = imageFileFormat
        this.fileName = "$name.${imageFileFormat.name.toLowerCase()}"
    }

    /**
     * Set storage location.
     */
    fun definePath(path: String) {
        prefixPath = path
    }

    fun defineEditPath() {
        prefixPath = localStorage.path.editedPath
    }

    fun defineUnRetouchedPath() {
        prefixPath = localStorage.path.unRetouchedPath
    }

    override fun upload(bytes: ByteArray): String {
        val pathAndFileName = "$prefixPath/$fileName"
        val file = File(pathAndFileName)
        val sink = Files.asByteSink(file, FileWriteMode.APPEND)
        sink.write(bytes)

        return makeImageUrl(pathAndFileName)
    }

    override fun upload(bufferedImage: BufferedImage): String {
        val out = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, imageFileFormat.name.toLowerCase(), out)
        return upload(out.toByteArray())
    }

    private fun makeImageUrl(pathAndFileName: String): String {
        return Paths.get(localStorage.url, pathAndFileName)
                .toAbsolutePath()
                .toString()
    }
}
