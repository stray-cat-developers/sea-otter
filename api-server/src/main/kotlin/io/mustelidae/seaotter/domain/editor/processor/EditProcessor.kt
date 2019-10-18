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

class EditProcessor {
    fun edit(bufferedImage: BufferedImage, queue: Queue<Option>): BufferedImage {

        var touchImage = bufferedImage

        for (option in queue) {
            touchImage = when (option) {
                is CropOption -> {
                    CropCommand(touchImage)
                            .apply { execute(option) }
                            .getBufferedImage()
                }
                is ResizeOption -> {
                    ResizeCommand(touchImage)
                            .apply { execute(option) }
                            .getBufferedImage()
                }
                is RotateOption -> {
                    RotateCommand(touchImage)
                            .apply { execute(option) }
                            .getBufferedImage()
                }
                else -> {
                    throw IllegalStateException("execute command is not define.")
                }
            }
        }
        return touchImage
    }
}
