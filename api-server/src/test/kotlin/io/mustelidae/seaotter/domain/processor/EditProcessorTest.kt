package io.mustelidae.seaotter.domain.processor

import io.mustelidae.seaotter.domain.command.CropOption
import io.mustelidae.seaotter.domain.command.RotateOption
import io.mustelidae.seaotter.domain.image.FlabbyImage
import io.mustelidae.seaotter.utils.getTestImageFileAsAbsolutePath
import io.mustelidae.seaotter.utils.write
import org.junit.jupiter.api.Test

internal class EditProcessorTest {

    private val inputPath = getTestImageFileAsAbsolutePath("denden_CMYK_2784x1856.jpg")

    @Test
    fun edit() {
        // Given
        val stepQueue = FirstInFirstOutStep().apply {
            cropByPosition(CropOption.Position.CENTER, 1000, 1000)
            rotateByFlip(RotateOption.Flip.HORZ)
            resizeByScale(90.0)
        }.queue
        var bufferedImage = FlabbyImage.getBufferedImage(inputPath)
        // When
        val processor = EditProcessor()
        val touchBufferedImage = processor.edit(bufferedImage, stepQueue)
        // Then
        write(touchBufferedImage, "touched-process-image.jpg")
    }
}
