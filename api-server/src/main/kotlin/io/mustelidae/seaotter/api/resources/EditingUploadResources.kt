package io.mustelidae.seaotter.api.resources

import com.fasterxml.jackson.annotation.JsonRawValue
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.mustelidae.seaotter.domain.delivery.Image
import io.mustelidae.seaotter.domain.editor.command.CropOption
import io.mustelidae.seaotter.domain.editor.command.RotateOption
import io.swagger.annotations.ApiModel
import java.net.URL

/**
 * Be sure to check EditingStepDeserializer when adding editing tools.
 */
class EditingUploadResources {

    @ApiModel("EditingUpload.Request")
    data class Request(
        @JsonRawValue
        @JsonDeserialize(using = EditingStepJsonDeserializer::class)
        val edits: List<OperationOption>,
        val base64: String,
        val hasOriginal: Boolean?
    )

    class Crop : Operation {
        data class Coordinate(
            val x1: Int,
            val y1: Int,
            val x2: Int,
            val y2: Int
        ) : OperationOption

        data class Position(
            val position: CropOption.Position,
            val width: Int,
            val height: Int
        ) : OperationOption

        data class PointScale(
            val x1: Int,
            val y1: Int,
            val width: Int,
            val height: Int
        ) : OperationOption
    }

    class Resize : Operation {
        data class Size(
            val width: Int,
            val height: Int,
            val keepRatio: Boolean
        ) : OperationOption

        data class Scale(
            val scale: Double
        ) : OperationOption
    }

    class Rotate : Operation {
        data class Angle(
            val degree: Double
        ) : OperationOption

        data class Flip(
            val flip: RotateOption.Flip
        ) : OperationOption
    }

    @ApiModel("EditingUpload.ReplyOnImage")
    data class ReplyOnImage(
        val width: Int,
        val height: Int,
        val path: String,
        val format: String,
        val original: Boolean,
        val histories: List<String>? = null
    ) {
        companion object {
            fun from(pair: Pair<Image, URL>, histories: List<String>): ReplyOnImage {
                val image = pair.first
                val url = pair.second.toString()

                return ReplyOnImage(
                    image.getMeta().width,
                    image.getMeta().height,
                    url,
                    image.getExtension(),
                    image.isOriginal,
                    if (image.isOriginal.not()) histories else null
                )
            }
        }
    }
}
