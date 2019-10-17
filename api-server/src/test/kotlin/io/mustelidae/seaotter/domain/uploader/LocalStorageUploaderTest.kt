package io.mustelidae.seaotter.domain.uploader

import io.mustelidae.seaotter.config.AppEnvironment
import io.mustelidae.seaotter.constant.ImageFileFormat
import io.mustelidae.seaotter.domain.image.FlabbyImage
import io.mustelidae.seaotter.utils.getTestImageFileAsAbsolutePath
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import java.io.File

internal class LocalStorageUploaderTest {

    @Test
    fun upload() {
        // Given
        val localStorage = AppEnvironment.LocalStorage().apply {
            url = "http://localhost:1111/test"
            path = AppEnvironment.Path().apply {
                editedPath = "out/image/edit"
                unRetouchedPath = "out/image/un-retouch"
            }
        }
        val inputPath = getTestImageFileAsAbsolutePath("denden_CMYK_2784x1856.jpg")
        val bufferedImage = FlabbyImage.getBufferedImage(inputPath)

        val file = File("${localStorage.path.editedPath}/sample.txt")
        if (file.exists().not()) file.mkdirs()

        // When
        val uploader = LocalStorageUploader(localStorage)
        uploader.initPath(localStorage.path.unRetouchedPath)
        uploader.initFile(ImageFileFormat.JPG, ObjectId().toString())
        uploader.upload(bufferedImage)
    }
}
