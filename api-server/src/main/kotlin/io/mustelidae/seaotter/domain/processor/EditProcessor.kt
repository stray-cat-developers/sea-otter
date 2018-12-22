package io.mustelidae.seaotter.domain.processor

import io.mustelidae.seaotter.domain.command.CropCommand
import io.mustelidae.seaotter.domain.command.CropOption
import io.mustelidae.seaotter.domain.command.Option
import io.mustelidae.seaotter.domain.command.ResizeCommand
import io.mustelidae.seaotter.domain.command.ResizeOption
import io.mustelidae.seaotter.domain.command.RotateCommand
import io.mustelidae.seaotter.domain.command.RotateOption
import java.awt.image.BufferedImage
import java.util.Queue


class EditProcessor {
    fun edit(bufferedImage: BufferedImage, queue:Queue<Option>){

        var touchImage = bufferedImage

        for(option in queue){
            touchImage = when (option){
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


    }
}
