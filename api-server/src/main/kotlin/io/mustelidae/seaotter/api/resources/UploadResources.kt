package io.mustelidae.seaotter.api.resources

import io.mustelidae.seaotter.domain.delivery.Image
import io.mustelidae.seaotter.domain.delivery.ShippingItem
import java.net.URL

class UploadResources {

    data class Reply(
        val images: List<ImageMetaData>
    ) {
        companion object {
            fun from(shippingItem: ShippingItem): Reply {
                val images = shippingItem.shippedImages.map { ImageMetaData.from(it.first, it.second) }
                return Reply(images)
            }
        }
    }

    data class ImageMetaData(
        val width: Int,
        val height: Int,
        val path: String,
        val format: String
    ) {
        companion object {
            fun from(image: Image, url: URL): ImageMetaData {
                return image.run {
                    ImageMetaData(
                        image.getMeta().width,
                        image.getMeta().height,
                        url.toString(),
                        image.getExtension()
                    )
                }
            }
        }
    }
}
