package io.mustelidae.seaotter.api.controller

import io.mustelidae.seaotter.api.resources.EditingStepValueDeserializer
import io.mustelidae.seaotter.api.resources.UploadResources
import io.mustelidae.seaotter.common.Replies
import io.mustelidae.seaotter.domain.delivery.EditImageDelivery
import io.mustelidae.seaotter.domain.delivery.Image
import io.mustelidae.seaotter.domain.editor.processor.EditOperation
import io.mustelidae.seaotter.utils.toReplies
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Api(tags = ["Editing Image Upload"])
@RestController
@RequestMapping("/upload/editing")
class EditingImageUploadController
@Autowired constructor(
    private val editImageDelivery: EditImageDelivery
) {

    private val editingStepValueDeserializer = EditingStepValueDeserializer()

    @ApiOperation("upload by multipart")
    @ApiImplicitParams(value = [
        ApiImplicitParam(paramType = "query", name = "crop"),
        ApiImplicitParam(paramType = "query", name = "resize"),
        ApiImplicitParam(paramType = "query", name = "rotate")
    ])
    @PostMapping(
        "multipart",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun upload(
        @RequestPart(required = true) multiPartFile: MultipartFile,
        @RequestParam allParams: Map<String, String>,
        @RequestParam(required = false) hasOriginal: Boolean?
    ): Replies<UploadResources.ReplyOnImage> {
        val operations = editingStepValueDeserializer.deserialize(allParams)
        val editOperation = EditOperation.from(operations)
        val image = Image.from(multiPartFile).apply {
            randomizeName()
        }

        val shippingItem = editImageDelivery.delivery(image, hasOriginal ?: false, editOperation)

        return shippingItem.shippedImages
            .map { UploadResources.ReplyOnImage.from(it) }
            .toReplies()
    }

    @ApiOperation("upload by base64")
    @ApiImplicitParams(value = [
        ApiImplicitParam(paramType = "query", name = "crop"),
        ApiImplicitParam(paramType = "query", name = "resize"),
        ApiImplicitParam(paramType = "query", name = "rotate")
    ])
    @PostMapping(
        "base64/form",
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun upload(
        @RequestParam base64: String,
        @RequestParam allParams: Map<String, String>,
        @RequestParam(required = false) hasOriginal: Boolean?
    ): Replies<UploadResources.ReplyOnImage> {
        val operations = editingStepValueDeserializer.deserialize(allParams)
        val editOperation = EditOperation.from(operations)
        val image = Image.from(base64)

        val shippingItem = editImageDelivery.delivery(image, hasOriginal ?: false, editOperation)

        return shippingItem.shippedImages
            .map { UploadResources.ReplyOnImage.from(it) }
            .toReplies()
    }
}
