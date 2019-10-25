package io.mustelidae.seaotter.domain.editor.processor

import io.mustelidae.seaotter.domain.editor.command.CropCommand
import io.mustelidae.seaotter.domain.editor.command.CropOption
import io.mustelidae.seaotter.domain.editor.command.Option
import io.mustelidae.seaotter.domain.editor.command.ResizeCommand
import io.mustelidae.seaotter.domain.editor.command.ResizeOption
import io.mustelidae.seaotter.domain.editor.command.RotateCommand
import io.mustelidae.seaotter.domain.editor.command.RotateOption
import java.awt.image.BufferedImage
import java.util.Queue

class EditProcessor(
    var bufferedImage: BufferedImage,
    val queue: Queue<Option>
) {

    fun processing() {
        for (option in queue) {
            bufferedImage = when (option) {
                is CropOption -> {
                    CropCommand(bufferedImage)
                        .apply { execute(option) }
                        .getBufferedImage()
                }
                is ResizeOption -> {
                    ResizeCommand(bufferedImage)
                        .apply { execute(option) }
                        .getBufferedImage()
                }
                is RotateOption -> {
                    RotateCommand(bufferedImage)
                        .apply { execute(option) }
                        .getBufferedImage()
                }
                else -> {
                    throw IllegalStateException("execute command is not define.")
                }
            }
        }
    }
}
