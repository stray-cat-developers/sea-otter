package io.mustelidae.seaotter.api.resources

import io.mustelidae.seaotter.domain.delivery.Image
import io.swagger.annotations.ApiModel
import java.net.URL

class UploadResources {

    @ApiModel("Upload.Request")
    data class Request(
        val base64: String,
        val hasOriginal: Boolean?
    )
    
    @ApiModel("Upload.RequestOnUrl")
    data class RequestOnUrl(
        val url: String,
        val hasOriginal: Boolean?
    )    

    @ApiModel("Upload.ReplyOnImage")
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
