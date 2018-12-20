package io.mustelidae.seaotter.domain.editor

import io.mustelidae.seaotter.domain.image.ImageScalingFlabbyImage
import io.mustelidae.seaotter.domain.image.ImgscalrFlabbyImage
import java.awt.image.BufferedImage

class ResizeCommand(private var bufferedImage: BufferedImage) : EditCommand<ResizeOption> {

    override fun execute(option: ResizeOption) {
        val flabbyImage = if (option.keepRatio)
            ImgscalrFlabbyImage(bufferedImage)
        else
            ImageScalingFlabbyImage(bufferedImage)

        if (option.scale != null)
            flabbyImage.resize(option.scale)
        else
            flabbyImage.resize(option.width!!, option.height!!)

        bufferedImage = flabbyImage.getBufferedImage()
    }

    override fun getBufferedImage(): BufferedImage = bufferedImage
}
