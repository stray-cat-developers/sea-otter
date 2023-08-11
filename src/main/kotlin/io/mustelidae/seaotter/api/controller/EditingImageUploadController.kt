package io.mustelidae.seaotter.api.controller

import io.mustelidae.seaotter.api.resources.EditingStepValueDeserializer
import io.mustelidae.seaotter.api.resources.EditingUploadResources
import io.mustelidae.seaotter.common.Replies
import io.mustelidae.seaotter.domain.delivery.EditImageDelivery
import io.mustelidae.seaotter.domain.delivery.Image
import io.mustelidae.seaotter.domain.editor.processor.EditOperation
import io.mustelidae.seaotter.utils.toReplies
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.enums.ParameterIn
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

@Tag(name = "Editing Image Upload")
@RestController
@RequestMapping("/upload/editing")
class EditingImageUploadController
@Autowired constructor(
    private val editImageDelivery: EditImageDelivery
) {

    private val editingStepValueDeserializer = EditingStepValueDeserializer()

    @Operation(summary = "upload by multipart")
    @Parameters(
        value = [
            Parameter(name = "1:crop", `in` = ParameterIn.QUERY),
            Parameter(name = "1:resize", `in` = ParameterIn.QUERY),
            Parameter(name = "1:rotate", `in` = ParameterIn.QUERY),
        ]
    )
    @PostMapping(
        "multipart",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun upload(
        @RequestPart(required = true) multiPartFile: MultipartFile,
        @RequestParam allParams: Map<String, String>,
        @RequestParam(required = false) hasOriginal: Boolean?,
        @RequestParam(required = false) topic: String?
    ): Replies<EditingUploadResources.ReplyOnImage> {
        val operations = editingStepValueDeserializer.deserialize(allParams)
        val editOperation = EditOperation.from(operations)
        val image = Image.from(multiPartFile).apply {
            randomizeName()
        }

        val shippingItem = editImageDelivery.delivery(image, hasOriginal ?: false, editOperation)

        return shippingItem.shippedImages
            .map { EditingUploadResources.ReplyOnImage.from(it, editOperation.histories) }
            .toReplies()
    }

    @Operation(summary = "upload by base64")
    @Parameters(
        value = [
            Parameter(`in` = ParameterIn.QUERY, name = "1:crop"),
            Parameter(`in` = ParameterIn.QUERY, name = "1:resize"),
            Parameter(`in` = ParameterIn.QUERY, name = "1:rotate")
        ]
    )
    @PostMapping(
        "base64/form",
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun upload(
        @RequestParam base64: String,
        @RequestParam allParams: Map<String, String>,
        @RequestParam(required = false) hasOriginal: Boolean?,
        @RequestParam(required = false) topic: String?
    ): Replies<EditingUploadResources.ReplyOnImage> {
        val operations = editingStepValueDeserializer.deserialize(allParams)
        val editOperation = EditOperation.from(operations)
        val image = Image.from(base64)

        val shippingItem = editImageDelivery.delivery(image, hasOriginal ?: false, editOperation)

        return shippingItem.shippedImages
            .map { EditingUploadResources.ReplyOnImage.from(it, editOperation.histories) }
            .toReplies()
    }

    @Operation(summary = "upload by safe url base64 using json")
    @PostMapping(
        "base64/json",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun upload(
        @RequestBody request: EditingUploadResources.Request,
        @RequestParam(required = false) topic: String?
    ): Replies<EditingUploadResources.ReplyOnImage> {
        val base64 = String(Base64Utils.decodeFromUrlSafeString(request.base64))
        val editOperation = EditOperation.from(request.edits)
        val image = Image.from(base64)

        val shippingItem = editImageDelivery.delivery(image, request.hasOriginal ?: false, editOperation)

        return shippingItem.shippedImages
            .map { EditingUploadResources.ReplyOnImage.from(it, editOperation.histories) }
            .toReplies()
    }
}
