package io.mustelidae.seaotter.api.resources

import io.mustelidae.seaotter.domain.delivery.Image
import java.net.URL

class UploadResources {

    data class ReplyOnImage(
        val width: Int,
        val height: Int,
        val path: String,
        val format: String,
        val original: Boolean
    ) {
        companion object {
            fun from(pair: Pair<Image, URL>): ReplyOnImage {
                val image = pair.first
                val url = pair.second.toString()
                return ReplyOnImage(
                    image.getMeta().width,
                    image.getMeta().height,
                    url,
                    image.getExtension(),
                    image.isOriginal
                )
            }
        }
    }
}
