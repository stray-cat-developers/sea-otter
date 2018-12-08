package io.mustelidae.seaotter.domain.uploader

import io.mustelidae.seaotter.config.OtterEnvironment
import io.mustelidae.seaotter.constant.ImageFileFormat
import io.mustelidae.seaotter.domain.image.FlabbyImage
import io.mustelidae.seaotter.utils.getTestImageFileAsAbsolutePath
import org.junit.jupiter.api.Test
import java.io.File

internal class LocalStorageUploaderTest {

    @Test
    fun upload() {
        // Given
        val localStorage = OtterEnvironment.LocalStorage().apply {
            url = "http://localhost:1111/test"
            path = OtterEnvironment.Path().apply {
                editedPath = "out/image/edit"
                unRetouchedPath = "out/image/un-retouch"
            }
        }
        val inputPath = getTestImageFileAsAbsolutePath("denden_CMYK_2784x1856.jpg")
        val bufferedImage = FlabbyImage.getBufferedImage(inputPath)

        val file = File("${localStorage.path.editedPath}/sample.txt")
        if (file.exists().not()) file.mkdir()

        // When
        val uploader = LocalStorageUploader(localStorage)
        uploader.defineEditPath()
        uploader.defineFile(ImageFileFormat.JPG)
        uploader.upload(bufferedImage)
    }
}
