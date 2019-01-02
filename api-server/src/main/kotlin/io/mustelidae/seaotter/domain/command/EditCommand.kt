package io.mustelidae.seaotter.domain.command

import java.awt.image.BufferedImage

interface EditCommand<T> {

    fun execute(option: T)

    fun getBufferedImage(): BufferedImage
}
