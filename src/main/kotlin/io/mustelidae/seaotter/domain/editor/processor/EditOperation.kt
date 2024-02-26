package io.mustelidae.seaotter.domain.editor.processor

import io.mustelidae.seaotter.api.resources.EditingUploadResources
import io.mustelidae.seaotter.api.resources.OperationOption
import io.mustelidae.seaotter.domain.editor.command.CropOption
import io.mustelidae.seaotter.domain.editor.command.Option
import io.mustelidae.seaotter.domain.editor.command.ResizeOption
import io.mustelidae.seaotter.domain.editor.command.RotateOption
import java.util.ArrayDeque
import java.util.Queue

class EditOperation {
    val queue: Queue<Option> = ArrayDeque()
    val histories = mutableListOf<String>()

    fun cropByCoordinates(x1: Int, y1: Int, x2: Int, y2: Int) {
        queue.offer(
            CropOption.Builder()
                .coordinates(x1, y1, x2, y2)
                .build(),
        )
    }

    fun cropByPosition(position: CropOption.Position, width: Int, height: Int) {
        queue.offer(
            CropOption.Builder()
                .position(position, width, height)
                .build(),
        )
    }

    fun cropByPoint(x1: Int, y1: Int, width: Int, height: Int) {
        queue.offer(
            CropOption.Builder()
                .pointScale(x1, y1, width, height)
                .build(),
        )
    }

    fun resizeByScale(scale: Double) {
        queue.offer(
            ResizeOption.Builder()
                .scale(scale)
                .build(),
        )
    }

    fun resizeBySize(width: Int, height: Int, keepRatio: Boolean) {
        queue.offer(
            ResizeOption.Builder()
                .size(width, height, keepRatio)
                .build(),
        )
    }

    fun rotateByDegree(degree: Double) {
        queue.offer(
            RotateOption.Builder()
                .angle(degree)
                .build(),
        )
    }

    fun rotateByFlip(flip: RotateOption.Flip) {
        queue.offer(
            RotateOption.Builder()
                .flip(flip)
                .build(),
        )
    }

    fun addHistory(description: String) {
        this.histories.add(description)
    }

    companion object {
        fun from(operationOptions: List<OperationOption>): EditOperation {
            val editOperation = EditOperation()

            for (operationOption in operationOptions) {
                when (operationOption) {
                    is EditingUploadResources.Crop.Coordinate -> {
                        operationOption.run {
                            editOperation.cropByCoordinates(x1, y1, x2, y2)
                        }
                    }
                    is EditingUploadResources.Crop.PointScale -> {
                        operationOption.run {
                            editOperation.cropByPoint(x1, y1, width, height)
                        }
                    }
                    is EditingUploadResources.Crop.Position -> {
                        operationOption.run {
                            editOperation.cropByPosition(position, width, height)
                        }
                    }
                    is EditingUploadResources.Resize.Scale -> {
                        operationOption.run {
                            editOperation.resizeByScale(scale)
                        }
                    }
                    is EditingUploadResources.Resize.Size -> {
                        operationOption.run {
                            editOperation.resizeBySize(width, height, keepRatio)
                        }
                    }
                    is EditingUploadResources.Rotate.Angle -> {
                        operationOption.run {
                            editOperation.rotateByDegree(degree)
                        }
                    }
                    is EditingUploadResources.Rotate.Flip -> {
                        operationOption.run {
                            editOperation.rotateByFlip(flip)
                        }
                    }
                }
            }

            return editOperation
        }
    }
}
