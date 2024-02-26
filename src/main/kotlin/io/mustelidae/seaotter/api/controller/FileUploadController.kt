package io.mustelidae.seaotter.api.controller

import io.mustelidae.seaotter.common.Reply
import io.mustelidae.seaotter.domain.delivery.PureDelivery
import io.mustelidae.seaotter.utils.toReply
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Tag(name = "File Upload")
@RestController
@RequestMapping("/upload/file")
class FileUploadController
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
        @RequestParam(required = false) topic: String?
    ): Reply<String> {
        val shippingItem = pureDelivery.delivery(multiPartFile)
        return shippingItem.shippedItem.first().second
            .toString()
            .toReply()
    }
}
