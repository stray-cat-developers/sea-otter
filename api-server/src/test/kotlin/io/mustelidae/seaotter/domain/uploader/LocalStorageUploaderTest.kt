package io.mustelidae.seaotter.domain.uploader

import io.mustelidae.seaotter.config.AppEnvironment
import io.mustelidae.seaotter.domain.delivery.Image
import io.mustelidae.seaotter.utils.getTestImageFileAsAbsolutePath
import org.junit.jupiter.api.Test
import java.io.File

internal class LocalStorageUploaderTest {

    @Test
    fun upload() {
        // Given
        val localStorage = AppEnvironment.LocalStorage().apply {
            url = "http://localhost:1111/test"
            path = "out/image"
        }

        val inputPath = getTestImageFileAsAbsolutePath("denden_CMYK_2784x1856.jpg")
        val image = Image.from(File(inputPath))

        val directoryPath = DirectoryPath(localStorage.path, null).apply {
            append(true)
        }

        val file = File("${directoryPath.getPath()}/sample.txt")
        if (file.exists().not()) file.mkdirs()

        // When
        val uploader = LocalStorageUploader(localStorage)
        uploader.upload(image)
    }
}
