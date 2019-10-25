package io.mustelidae.seaotter.api.resources

import com.github.kittinunf.fuel.util.encodeBase64
import io.mustelidae.seaotter.domain.delivery.Image
import io.swagger.annotations.ApiModel
import org.springframework.util.Base64Utils
import java.net.URL

class UploadResources {

    @ApiModel("Upload.Request")
    data class Request(
        val base64: String,
        val hasOriginal: Boolean?
    ) {
        fun convertBase64SafeUrlToBase64(): String {
            val index = base64.lastIndexOf(',')
            val base64SafeUrl = base64.substring(index + 1)
            val header = base64.substring(0, index).replace('_', '/')

            val byteArray = Base64Utils.decodeFromUrlSafeString(base64SafeUrl)
            val encodedBase64 = String(byteArray.encodeBase64())

            return "$header,$encodedBase64"
        }
    }

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
