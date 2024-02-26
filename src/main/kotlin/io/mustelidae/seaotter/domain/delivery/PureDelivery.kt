package io.mustelidae.seaotter.domain.delivery

import io.mustelidae.seaotter.constant.ImageFileFormat
import io.mustelidae.seaotter.domain.editor.image.ThumbnailatorFlabbyImage
import io.mustelidae.seaotter.domain.uploader.UploadHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.net.URL

@Service
class PureDelivery
@Autowired constructor(
    private val uploadHandler: UploadHandler
) {

    fun delivery(image: Image, hasOriginal: Boolean): ShippingItem<Image> {
        val shippingItem = ShippingItem(image)
        if (hasOriginal) {
            shippingItem.shippedItem.add(upload(image))
        }

        shippingItem.shippedItem.add(compressAndUpload(image))
        return shippingItem
    }

    private fun upload(image: Image): Pair<Image, URL> {
        val url = uploadHandler.upload(image)
        return Pair(image, url)
    }

    private fun compressAndUpload(image: Image): Pair<Image, URL> {
        val compressedBufferedImage = ThumbnailatorFlabbyImage(image.bufferedImage).run {
            compress(0.9)
            getBufferedImage()
        }

        val compressedImage = Image(compressedBufferedImage, image.name, ImageFileFormat.JPG, false)

        return upload(compressedImage)
    }

    fun delivery(multipartFile: MultipartFile): ShippingItem<MultipartFile> {
        val shippingItem = ShippingItem(multipartFile)

        shippingItem.shippedItem.add(
            Pair(multipartFile, uploadHandler.upload(multipartFile))
        )

        return shippingItem
    }
}
