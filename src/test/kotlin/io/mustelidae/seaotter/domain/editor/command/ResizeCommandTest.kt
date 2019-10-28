package io.mustelidae.seaotter.domain.editor.command

import com.google.common.truth.Truth.assertThat
import io.mustelidae.seaotter.domain.delivery.Image
import io.mustelidae.seaotter.utils.getTestImageFileAsAbsolutePath
import org.junit.jupiter.api.Test
import java.io.File

internal class ResizeCommandTest {

    private val inputPath = getTestImageFileAsAbsolutePath("denden_RGB_2784x1856.jpg")

    @Test
    fun reduce() {
        // Given
        val option = ResizeOption.Builder().scale(50.0).build()
        var bufferedImage = Image.from(File(inputPath)).bufferedImage
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
        var bufferedImage = Image.from(File(inputPath)).bufferedImage
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
        var bufferedImage = Image.from(File(inputPath)).bufferedImage
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
        var bufferedImage = Image.from(File(inputPath)).bufferedImage
        val resizeCommand = ResizeCommand(bufferedImage)
        // When
        resizeCommand.execute(option)
        bufferedImage = resizeCommand.getBufferedImage()
        assertThat(bufferedImage.width).isEqualTo(1000)
        assertThat(bufferedImage.height).isEqualTo(667)
    }
}
