package io.mustelidae.seaotter.domain.editor

import java.awt.image.BufferedImage

interface EditCommand<T> {

    fun execute(option: T)

    fun getBufferedImage(): BufferedImage
}
