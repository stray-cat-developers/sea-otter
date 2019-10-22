package io.mustelidae.seaotter.api.controller

import io.mustelidae.seaotter.api.resources.UploadResources
import io.mustelidae.seaotter.common.Reply
import io.mustelidae.seaotter.domain.delivery.Image
import io.mustelidae.seaotter.domain.delivery.PureDelivery
import io.mustelidae.seaotter.utils.toReply
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/upload/simple")
class SimpleUploadController
@Autowired constructor(
    private val pureDelivery: PureDelivery
) {

    @PostMapping(
        "multipart",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun upload(
        @RequestPart(required = true) multiPartFile: MultipartFile,
        @RequestParam(required = false) hasOriginal: Boolean?
    ): Reply<UploadResources.Reply> {

        val image = Image.from(multiPartFile)

        val shippingItem = pureDelivery.delivery(image, hasOriginal ?: false)

        return UploadResources.Reply.from(shippingItem).toReply()
    }
}
