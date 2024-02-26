package io.mustelidae.seaotter.api.controller

import io.mustelidae.seaotter.api.resources.UploadResources
import io.mustelidae.seaotter.common.Replies
import io.mustelidae.seaotter.domain.delivery.Image
import io.mustelidae.seaotter.domain.delivery.PureDelivery
import io.mustelidae.seaotter.utils.toReplies
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.util.Base64Utils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.net.URL

@Tag(name = "Simple Image Upload")
@RestController
@RequestMapping("/upload/simple")
class SimpleUploadController
@Autowired constructor(
    private val pureDelivery: PureDelivery
) {

    @Operation(summary = "upload by multipart")
    @PostMapping(
        "multipart",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun upload(
        @RequestPart(required = true) multiPartFile: MultipartFile,
        @RequestParam(required = false) hasOriginal: Boolean?,
        @RequestParam(required = false) topic: String?
    ): Replies<UploadResources.ReplyOnImage> {

        val image = Image.from(multiPartFile).apply {
            randomizeName()
        }

        val shippingItem = pureDelivery.delivery(image, hasOriginal ?: false)

        return shippingItem.shippedItem
            .map { UploadResources.ReplyOnImage.from(it) }
            .toReplies()
    }

    @Operation(summary = "upload by base64")
    @PostMapping(
        "base64/form",
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun upload(
        @RequestParam base64: String,
        @RequestParam(required = false) hasOriginal: Boolean?,
        @RequestParam(required = false) topic: String?
    ): Replies<UploadResources.ReplyOnImage> {

        val image = Image.from(base64)
        val shippingItem = pureDelivery.delivery(image, hasOriginal ?: false)

        return shippingItem.shippedItem
            .map { UploadResources.ReplyOnImage.from(it) }
            .toReplies()
    }

    @Operation(summary = "upload by base64 safe url using json")
    @PostMapping(
        "base64/json",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun upload(
        @RequestBody request: UploadResources.Request
    ): Replies<UploadResources.ReplyOnImage> {

        val base64 = String(Base64Utils.decodeFromUrlSafeString(request.base64))
        val image = Image.from(base64)
        val shippingItem = pureDelivery.delivery(image, request.hasOriginal ?: false)

        return shippingItem.shippedItem
            .map { UploadResources.ReplyOnImage.from(it) }
            .toReplies()
    }

    @Operation(summary = "upload using url")
    @PostMapping(
        "url",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun upload(
        @RequestBody request: UploadResources.RequestOnUrl
    ): Replies<UploadResources.ReplyOnImage> {
        val image = Image.from(URL(request.url))
        val shippingItem = pureDelivery.delivery(image, request.hasOriginal ?: false)

        return shippingItem.shippedItem
            .map { UploadResources.ReplyOnImage.from(it) }
            .toReplies()
    }
}
