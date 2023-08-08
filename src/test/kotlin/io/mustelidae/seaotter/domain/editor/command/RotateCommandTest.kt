package io.mustelidae.seaotter.domain.editor.command

import io.kotest.matchers.shouldBe
import io.mustelidae.seaotter.domain.delivery.Image
import io.mustelidae.seaotter.utils.getTestImageFileAsAbsolutePath
import io.mustelidae.seaotter.utils.write
import org.junit.jupiter.api.Test
import java.io.File

internal class RotateCommandTest {

    private val inputPath = getTestImageFileAsAbsolutePath("denden_RGB_2784x1856.jpg")

    @Test
    fun rotateBy90Degree() {
        // Given
        val option = RotateOption.Builder().angle(90.0).build()
        var bufferedImage = Image.from(File(inputPath)).bufferedImage
        val rotateCommand = RotateCommand(bufferedImage)
        // When
        rotateCommand.execute(option)
        bufferedImage = rotateCommand.getBufferedImage()
        // Then
        bufferedImage.width shouldBe 1856
        bufferedImage.height shouldBe 2784

        write(bufferedImage, "rotate90")
    }

    @Test
    fun rotateBy60Degree() {
        // Given
        val option = RotateOption.Builder().angle(60.0).build()
        var bufferedImage = Image.from(File(inputPath)).bufferedImage
        val rotateCommand = RotateCommand(bufferedImage)
        // When
        rotateCommand.execute(option)
        bufferedImage = rotateCommand.getBufferedImage()
        // Then
        bufferedImage.width shouldBe 2999
        bufferedImage.height shouldBe 3339
    }

    @Test
    fun flip() {
        // Given
        val option = RotateOption.Builder().flip(RotateOption.Flip.VERT).build()
        var bufferedImage = Image.from(File(inputPath)).bufferedImage
        val rotateCommand = RotateCommand(bufferedImage)
        // When
        rotateCommand.execute(option)
        bufferedImage = rotateCommand.getBufferedImage()
        // Then
        bufferedImage.width shouldBe 2784
        bufferedImage.height shouldBe 1856
    }
}
