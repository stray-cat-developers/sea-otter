package io.mustelidae.seaotter.api.resources

import io.mustelidae.seaotter.domain.editor.command.CropOption
import io.mustelidae.seaotter.domain.editor.command.RotateOption

class EditingStepValueDeserializer {

    fun deserialize(allParams: Map<String, String>): List<OperationOption> {
        val edits: MutableList<OperationOption> = mutableListOf()
        val operationParameters = allParams.map { OperationParameter.from(it.key, it.value) }.sortedBy { it?.order }

        for (operationParameter in operationParameters) {
            operationParameter?.let { edits.add(it.convertOption()) }
        }

        return edits
    }

    private data class OperationParameter(
        val order: Int,
        val name: String,
        val operationOption: String,
        val value: String
    ) {

        fun convertOption(): OperationOption {
            return when (name) {
                EditingUploadResources.Crop::class.simpleName!!.decapitalize() -> {
                    toCrop()
                }
                EditingUploadResources.Resize::class.simpleName!!.decapitalize() -> {
                    toResize()
                }
                EditingUploadResources.Rotate::class.simpleName!!.decapitalize() -> {
                    toRotate()
                }
                else -> throw IllegalArgumentException("$name operation is not support")
            }
        }

        private fun toResize(): OperationOption {
            val values = value.split(',')
            return when (operationOption) {
                EditingUploadResources.Resize.Scale::class.simpleName!!.decapitalize() -> {
                    EditingUploadResources.Resize.Scale(
                        values[0].toDouble()
                    )
                }
                EditingUploadResources.Resize.Size::class.simpleName!!.decapitalize() -> {
                    EditingUploadResources.Resize.Size(
                        values[0].toInt(),
                        values[1].toInt(),
                        values[2].toBoolean()
                    )
                }
                else -> throw IllegalArgumentException("$operationOption option is not support")
            }
        }

        private fun toRotate(): OperationOption {
            val values = value.split(',')
            return when (operationOption) {
                EditingUploadResources.Rotate.Angle::class.simpleName!!.decapitalize() -> {
                    EditingUploadResources.Rotate.Angle(
                        values[0].toDouble()
                    )
                }
                EditingUploadResources.Rotate.Flip::class.simpleName!!.decapitalize() -> {
                    EditingUploadResources.Rotate.Flip(
                        RotateOption.Flip.valueOf(values[0])
                    )
                }
                else -> throw IllegalArgumentException("$operationOption option is not support")
            }
        }

        private fun toCrop(): OperationOption {
            val values = value.split(',')
            return when (operationOption) {
                EditingUploadResources.Crop.Coordinate::class.simpleName!!.decapitalize() -> {
                    EditingUploadResources.Crop.Coordinate(
                        values[0].toInt(),
                        values[1].toInt(),
                        values[2].toInt(),
                        values[3].toInt()
                    )
                }
                EditingUploadResources.Crop.PointScale::class.simpleName!!.decapitalize() -> {
                    EditingUploadResources.Crop.PointScale(
                        values[0].toInt(),
                        values[1].toInt(),
                        values[2].toInt(),
                        values[3].toInt()
                    )
                }
                EditingUploadResources.Crop.Position::class.simpleName!!.decapitalize() -> {
                    EditingUploadResources.Crop.Position(
                        CropOption.Position.valueOf(values[0]),
                        values[1].toInt(),
                        values[2].toInt()
                    )
                }
                else -> throw IllegalArgumentException("$operationOption option is not support")
            }
        }

        companion object {
            private val supportOperations = listOf(
                EditingUploadResources.Crop::class.simpleName!!.decapitalize(),
                EditingUploadResources.Resize::class.simpleName!!.decapitalize(),
                EditingUploadResources.Rotate::class.simpleName!!.decapitalize()
            )

            fun from(key: String, value: String): OperationParameter? {
                val keys = key.split(':')
                val values = value.split(':')

                if (keys.size != 2 || values.size != 2)
                    return null

                if (supportOperations.contains(keys[1]).not())
                    return null

                return OperationParameter(
                    keys[0].toInt(),
                    keys[1],
                    values[0],
                    values[1]
                )
            }
        }
    }
}
