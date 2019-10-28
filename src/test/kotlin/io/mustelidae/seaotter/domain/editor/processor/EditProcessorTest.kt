package io.mustelidae.seaotter.domain.editor.processor

import io.mustelidae.seaotter.domain.delivery.Image
import io.mustelidae.seaotter.domain.editor.command.CropOption
import io.mustelidae.seaotter.domain.editor.command.RotateOption
import io.mustelidae.seaotter.utils.getTestImageFileAsAbsolutePath
import io.mustelidae.seaotter.utils.write
import org.junit.jupiter.api.Test
import java.io.File

internal class EditProcessorTest {

    private val inputPath = getTestImageFileAsAbsolutePath("denden_CMYK_2784x1856.jpg")

    @Test
    fun edit() {
        // Given

        val editOperation = EditOperation().apply {
            cropByPosition(CropOption.Position.CENTER, 1000, 1000)
            rotateByFlip(RotateOption.Flip.HORZ)
            resizeByScale(90.0)
        }

        val bufferedImage = Image.from(File(inputPath)).bufferedImage
        // When
        val processor = EditProcessor(bufferedImage, editOperation)
        processor.processing()
        // Then
        write(processor.bufferedImage, "touched-process-image.jpg")
    }
}
