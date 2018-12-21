package io.mustelidae.seaotter.domain.command

import io.mustelidae.seaotter.domain.command.CropOption.Type.COORDINATES
import io.mustelidae.seaotter.domain.command.CropOption.Type.POINT_SCALE
import io.mustelidae.seaotter.domain.command.CropOption.Type.POSITION
import io.mustelidae.seaotter.domain.image.ImageScalingFlabbyImage
import io.mustelidae.seaotter.domain.image.ImgscalrFlabbyImage
import java.awt.image.BufferedImage

class CropCommand(private var bufferedImage: BufferedImage) : EditCommand<CropOption> {
    override fun execute(option: CropOption) {

        bufferedImage = when (option.type) {
            POINT_SCALE -> {
                ImgscalrFlabbyImage(bufferedImage)
                        .apply { cropByPointScale(option.x1!!, option.y1!!, option.width!!, option.height!!) }
                        .getBufferedImage()
            }
            COORDINATES -> {
                ImageScalingFlabbyImage(bufferedImage)
                        .apply { crop(option.x1!!, option.y1!!, option.x2!!, option.y2!!) }
                        .getBufferedImage()
            }
            POSITION -> {
                val flabbyImage = if (option.position == CropOption.Position.CENTER)
                    ImageScalingFlabbyImage(bufferedImage)
                else
                    ImgscalrFlabbyImage(bufferedImage)

                flabbyImage.crop(option.width!!, option.height!!)
                flabbyImage.getBufferedImage()
            }
        }
    }

    override fun getBufferedImage(): BufferedImage = bufferedImage
}
