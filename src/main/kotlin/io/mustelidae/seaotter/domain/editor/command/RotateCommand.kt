package io.mustelidae.seaotter.domain.editor.command

import io.mustelidae.seaotter.domain.editor.image.ImgscalrFlabbyImage
import io.mustelidae.seaotter.domain.editor.image.ThumbnailatorFlabbyImage
import org.imgscalr.Scalr
import java.awt.image.BufferedImage

class RotateCommand(private var bufferedImage: BufferedImage) : EditCommand<RotateOption> {

    override fun execute(option: RotateOption) {
        option.flip?.let {
            val flabbyImage = ImgscalrFlabbyImage(bufferedImage)
            when (it) {
                RotateOption.Flip.HORZ -> flabbyImage.rotate(Scalr.Rotation.FLIP_HORZ)
                RotateOption.Flip.VERT -> flabbyImage.rotate(Scalr.Rotation.FLIP_VERT)
            }
            bufferedImage = flabbyImage.getBufferedImage()
        }

        option.angle?.let { angle ->
            val scalrRotation = convertScalrRotation(angle)

            bufferedImage = if (scalrRotation != null) {
                ImgscalrFlabbyImage(bufferedImage)
                    .apply {
                        rotate(scalrRotation)
                    }.getBufferedImage()
            } else {
                ThumbnailatorFlabbyImage(bufferedImage)
                    .apply {
                        rotate(angle)
                    }.getBufferedImage()
            }
        }
    }

    override fun getBufferedImage(): BufferedImage = bufferedImage

    private fun convertScalrRotation(angle: Double): Scalr.Rotation? {
        val intOfAngle = angle.toInt()
        if (intOfAngle == 90)
            return Scalr.Rotation.CW_90
        if (intOfAngle == 180)
            return Scalr.Rotation.CW_180
        if (intOfAngle == 270)
            return Scalr.Rotation.CW_270

        return null
    }
}
