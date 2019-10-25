package io.mustelidae.seaotter.api.resources

import com.fasterxml.jackson.annotation.JsonRawValue
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.mustelidae.seaotter.domain.editor.command.CropOption
import io.mustelidae.seaotter.domain.editor.command.RotateOption

/**
 * Be sure to check EditingStepDeserializer when adding editing tools.
 */
class EditingUploadResources {

    data class Request(
        @JsonRawValue
        @JsonDeserialize(using = EditingStepJsonDeserializer::class)
        val edits: List<OperationOption>
    )

    class Crop {
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

    class Resize {
        data class Size(
            val width: Int,
            val height: Int,
            val keepRatio: Boolean
        ) : OperationOption

        data class Scale(
            val scale: Double
        ) : OperationOption
    }

    class Rotate {
        data class Angle(
            val degree: Double
        ) : OperationOption

        data class Flip(
            val flip: RotateOption.Flip
        ) : OperationOption
    }
}
