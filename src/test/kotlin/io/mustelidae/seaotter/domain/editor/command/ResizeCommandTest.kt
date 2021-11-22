package io.mustelidae.seaotter.domain.editor.command

import io.kotlintest.shouldBe
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
        bufferedImage.width shouldBe 1392
        bufferedImage.height shouldBe 928
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
        bufferedImage.width shouldBe 4176
        bufferedImage.height shouldBe 2784
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
        bufferedImage.width shouldBe 1000
        bufferedImage.height shouldBe 1000
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
        bufferedImage.width shouldBe 1000
        bufferedImage.height shouldBe 667
    }
}
