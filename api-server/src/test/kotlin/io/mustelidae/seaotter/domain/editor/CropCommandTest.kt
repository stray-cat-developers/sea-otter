package io.mustelidae.seaotter.domain.editor

import com.google.common.truth.Truth.assertThat
import io.mustelidae.seaotter.domain.image.FlabbyImage
import io.mustelidae.seaotter.utils.getTestImageFileAsAbsolutePath
import org.junit.jupiter.api.Test

internal class CropCommandTest {

    @Test
    fun execute() {
        // Given
        val inputPath = getTestImageFileAsAbsolutePath("denden_CMYK_2784x1856.jpg")
        var bufferedImage = FlabbyImage.getBufferedImage(inputPath)
        // When
        val cropCommand = CropCommand(bufferedImage)
        val option = CropOption.Builder().position(CropOption.Position.CENTER, 300, 300).build()
        cropCommand.execute(option)
        bufferedImage = cropCommand.getBufferedImage()
        // Then
        assertThat(bufferedImage.width).isEqualTo(300)
        assertThat(bufferedImage.height).isEqualTo(300)
    }

    @Test
    fun test() {
        val inputPath = getTestImageFileAsAbsolutePath("denden_CMYK_2784x1856.jpg")
        val bufferedImage = FlabbyImage.getBufferedImage(inputPath)
    }
}
