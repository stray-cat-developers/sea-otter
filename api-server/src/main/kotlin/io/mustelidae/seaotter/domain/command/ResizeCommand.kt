package io.mustelidae.seaotter.domain.command

import io.mustelidae.seaotter.domain.image.ImageScalingFlabbyImage
import io.mustelidae.seaotter.domain.image.ImgscalrFlabbyImage
import java.awt.image.BufferedImage

class ResizeCommand(private var bufferedImage: BufferedImage) : EditCommand<ResizeOption> {

    override fun execute(option: ResizeOption) {
        if (option.scale != null) {
            if (option.scale == 100.0)
                return

            val flabbyImage = if (option.scale > 100.0)
                ImageScalingFlabbyImage(bufferedImage)
            else
                ImgscalrFlabbyImage(bufferedImage)

            flabbyImage.resize(option.scale)
            bufferedImage = flabbyImage.getBufferedImage()
        } else {
            if (option.width == bufferedImage.width && option.height == bufferedImage.height)
                return

            val flabbyImage = if (option.keepRatio)
                ImgscalrFlabbyImage(bufferedImage)
            else
                ImageScalingFlabbyImage(bufferedImage)

            flabbyImage.resize(option.width!!, option.height!!)
            bufferedImage = flabbyImage.getBufferedImage()
        }
    }

    override fun getBufferedImage(): BufferedImage = bufferedImage
}
