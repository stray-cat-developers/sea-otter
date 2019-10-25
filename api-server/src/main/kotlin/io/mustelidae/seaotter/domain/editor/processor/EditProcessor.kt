package io.mustelidae.seaotter.domain.editor.processor

import io.mustelidae.seaotter.domain.editor.command.CropCommand
import io.mustelidae.seaotter.domain.editor.command.CropOption
import io.mustelidae.seaotter.domain.editor.command.ResizeCommand
import io.mustelidae.seaotter.domain.editor.command.ResizeOption
import io.mustelidae.seaotter.domain.editor.command.RotateCommand
import io.mustelidae.seaotter.domain.editor.command.RotateOption
import java.awt.image.BufferedImage

class EditProcessor(
    var bufferedImage: BufferedImage,
    private val editOperation: EditOperation
) {

    fun processing() {
        for (option in editOperation.queue) {
            bufferedImage = when (option) {
                is CropOption -> {
                    editOperation.addHistory("crop")
                    CropCommand(bufferedImage)
                        .apply { execute(option) }
                        .getBufferedImage()
                }
                is ResizeOption -> {
                    editOperation.addHistory("resize")
                    ResizeCommand(bufferedImage)
                        .apply { execute(option) }
                        .getBufferedImage()
                }
                is RotateOption -> {
                    editOperation.addHistory("rotate")
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
