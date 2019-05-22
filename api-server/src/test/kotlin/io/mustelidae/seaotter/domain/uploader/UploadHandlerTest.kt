package io.mustelidae.seaotter.domain.uploader

import io.mustelidae.seaotter.config.OtterEnvironment
import io.mustelidae.seaotter.constant.ImageFileFormat
import io.mustelidae.seaotter.domain.image.FlabbyImage
import io.mustelidae.seaotter.utils.getTestImageFileAsAbsolutePath
import org.junit.jupiter.api.Test
import java.io.File

internal class UploadHandlerTest {

    @Test
    fun upload() {
        // Given
        val otterEnvironment = OtterEnvironment().apply {
            this.uploader = "local"
            this.localStorage = OtterEnvironment.LocalStorage().apply {
                url = "http://localhost:1111/test"
                path = OtterEnvironment.Path().apply {
                    editedPath = File("out/image/edit").absolutePath
                    unRetouchedPath = File("out/image/un-retouch").absolutePath
                }
                shardType = "date"
            }
        }

        val uploadFile = UploadFile(
                FlabbyImage.getBufferedImage(getTestImageFileAsAbsolutePath("denden_CMYK_2784x1856.jpg")),
                ImageFileFormat.JPG,
                true
        )
        val uploadHandler = UploadHandler(otterEnvironment)
        // When
        uploadHandler.upload(uploadFile)
    }
}