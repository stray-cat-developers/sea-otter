package io.mustelidae.seaotter.domain.delivery

import io.mustelidae.seaotter.constant.ImageFileFormat
import io.mustelidae.seaotter.domain.editor.processor.EditOperation
import io.mustelidae.seaotter.domain.editor.processor.EditProcessor
import io.mustelidae.seaotter.domain.uploader.UploadHandler
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.URL

@Service
class EditImageDelivery
@Autowired constructor(
    private val uploadHandler: UploadHandler,
) {

    fun delivery(image: Image, hasOriginal: Boolean, editOperation: EditOperation): ShippingItem<Image> {
        val shippingItem = ShippingItem(image)
        if (hasOriginal) {
            shippingItem.shippedItem.add(upload(image))
        }

        val processor = EditProcessor(image.bufferedImage, editOperation)
        processor.processing()

        val touchedImage = Image(processor.bufferedImage, ObjectId().toString(), ImageFileFormat.JPG, false).apply {
            reviseFormat()
        }

        shippingItem.shippedItem.add(upload(touchedImage))

        return shippingItem
    }

    private fun upload(image: Image): Pair<Image, URL> {
        val url = uploadHandler.upload(image)
        return Pair(image, url)
    }
}
