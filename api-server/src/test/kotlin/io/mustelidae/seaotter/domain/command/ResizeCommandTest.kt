package io.mustelidae.seaotter.domain.command

import com.google.common.truth.Truth.assertThat
import io.mustelidae.seaotter.domain.image.FlabbyImage
import io.mustelidae.seaotter.utils.getTestImageFileAsAbsolutePath
import org.junit.jupiter.api.Test

internal class ResizeCommandTest {

    private val inputPath = getTestImageFileAsAbsolutePath("denden_RGB_2784x1856.jpg")

    @Test
    fun reduce() {
        // Given
        val option = ResizeOption.Builder().scale(50.0).build()
        var bufferedImage = FlabbyImage.getBufferedImage(inputPath)
        val resizeCommand = ResizeCommand(bufferedImage)
        // When
        resizeCommand.execute(option)
        bufferedImage = resizeCommand.getBufferedImage()
        // Then
        assertThat(bufferedImage.width).isEqualTo(1392)
        assertThat(bufferedImage.height).isEqualTo(928)
    }

    @Test
    fun enlarge() {
        // Given
        val option = ResizeOption.Builder().scale(150.0).build()
        var bufferedImage = FlabbyImage.getBufferedImage(inputPath)
        val resizeCommand = ResizeCommand(bufferedImage)
        // When
        resizeCommand.execute(option)
        bufferedImage = resizeCommand.getBufferedImage()
        // Then
        assertThat(bufferedImage.width).isEqualTo(4176)
        assertThat(bufferedImage.height).isEqualTo(2784)
    }

    @Test
    fun ignoreTheRatioAndReduce() {
        // Given
        val option = ResizeOption.Builder().size(1000, 1000, false).build()
        var bufferedImage = FlabbyImage.getBufferedImage(inputPath)
        val resizeCommand = ResizeCommand(bufferedImage)
        // When
        resizeCommand.execute(option)
        bufferedImage = resizeCommand.getBufferedImage()
        // Then
        assertThat(bufferedImage.width).isEqualTo(1000)
        assertThat(bufferedImage.height).isEqualTo(1000)
    }

    @Test
    fun keepTheRatioAndReduce() {
        // Given
        val option = ResizeOption.Builder().size(1000, 1000, true).build()
        var bufferedImage = FlabbyImage.getBufferedImage(inputPath)
        val resizeCommand = ResizeCommand(bufferedImage)
        // When
        resizeCommand.execute(option)
        bufferedImage = resizeCommand.getBufferedImage()
        assertThat(bufferedImage.width).isEqualTo(1000)
        assertThat(bufferedImage.height).isEqualTo(667)
    }
}
