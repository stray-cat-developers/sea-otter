package io.mustelidae.seaotter.domain.command

import com.google.common.truth.Truth.assertThat
import io.mustelidae.seaotter.domain.image.FlabbyImage
import io.mustelidae.seaotter.utils.getTestImageFileAsAbsolutePath
import io.mustelidae.seaotter.utils.write
import org.junit.jupiter.api.Test

internal class RotateCommandTest {

    private val inputPath = getTestImageFileAsAbsolutePath("denden_RGB_2784x1856.jpg")

    @Test
    fun rotateBy90Degree() {
        // Given
        val option = RotateOption.Builder().angle(90.0).build()
        var bufferedImage = FlabbyImage.getBufferedImage(inputPath)
        val rotateCommand = RotateCommand(bufferedImage)
        // When
        rotateCommand.execute(option)
        bufferedImage = rotateCommand.getBufferedImage()
        // Then
        assertThat(bufferedImage.width).isEqualTo(1856)
        assertThat(bufferedImage.height).isEqualTo(2784)

        write(bufferedImage, "rotate90")
    }

    @Test
    fun rotateBy60Degree() {
        // Given
        val option = RotateOption.Builder().angle(60.0).build()
        var bufferedImage = FlabbyImage.getBufferedImage(inputPath)
        val rotateCommand = RotateCommand(bufferedImage)
        // When
        rotateCommand.execute(option)
        bufferedImage = rotateCommand.getBufferedImage()
        // Then
        assertThat(bufferedImage.width).isEqualTo(2999)
        assertThat(bufferedImage.height).isEqualTo(3339)
    }

    @Test
    fun flip() {
        // Given
        val option = RotateOption.Builder().flip(RotateOption.Flip.VERT).build()
        var bufferedImage = FlabbyImage.getBufferedImage(inputPath)
        val rotateCommand = RotateCommand(bufferedImage)
        // When
        rotateCommand.execute(option)
        bufferedImage = rotateCommand.getBufferedImage()
        // Then
        assertThat(bufferedImage.width).isEqualTo(2784)
        assertThat(bufferedImage.height).isEqualTo(1856)
    }
}
