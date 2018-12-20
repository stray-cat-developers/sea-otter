package io.mustelidae.seaotter.domain.editor

import io.mustelidae.seaotter.domain.editor.CropOption.Position
import io.mustelidae.seaotter.domain.editor.CropOption.Type.COORDINATES
import io.mustelidae.seaotter.domain.editor.CropOption.Type.POINT_SCALE
import io.mustelidae.seaotter.domain.editor.CropOption.Type.POSITION
import io.mustelidae.seaotter.domain.image.ImageScalingFlabbyImage
import io.mustelidae.seaotter.domain.image.ImgscalrFlabbyImage
import java.awt.image.BufferedImage

class CropCommand(private var bufferedImage: BufferedImage) : EditCommand<CropOption> {
    override fun execute(option: CropOption) {

        bufferedImage = if (POINT_SCALE == option.type) {
            val flabbyImage = ImgscalrFlabbyImage(bufferedImage)
            flabbyImage.cropByPointScale(option.x1!!, option.y1!!, option.width!!, option.height!!)
            flabbyImage.getBufferedImage()
        } else {
            val flabbyImage = when (option.type) {
                COORDINATES, POINT_SCALE -> {
                    ImageScalingFlabbyImage(bufferedImage)
                }
                POSITION -> {
                    if (option.position == Position.CENTER)
                        ImageScalingFlabbyImage(bufferedImage)
                    else
                        ImgscalrFlabbyImage(bufferedImage)
                }
            }
            flabbyImage.crop(option.width!!, option.height!!)
            flabbyImage.getBufferedImage()
        }
    }

    override fun getBufferedImage(): BufferedImage = bufferedImage
}
