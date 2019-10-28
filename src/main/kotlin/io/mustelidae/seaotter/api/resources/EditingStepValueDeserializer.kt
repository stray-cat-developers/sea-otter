package io.mustelidae.seaotter.api.resources

import io.mustelidae.seaotter.domain.editor.command.CropOption
import io.mustelidae.seaotter.domain.editor.command.RotateOption
import kotlin.reflect.KClass

class EditingStepValueDeserializer {

    private val supportOperations = listOf(
        EditingUploadResources.Crop::class.simpleName!!.decapitalize(),
        EditingUploadResources.Resize::class.simpleName!!.decapitalize(),
        EditingUploadResources.Rotate::class.simpleName!!.decapitalize()
    )

    fun deserialize(allParams: Map<String, String>): List<OperationOption> {
        val edits: MutableList<OperationOption> = mutableListOf()
        val operations = allParams
            .filter { supportOperations.contains(it.key) }
            .map { it.key to split(it.value) }

        for (operation in operations) {
            val operationName = operation.first
            val pairOfOption = operation.second

            convertCrop(operationName, pairOfOption, edits)
            convertResize(operationName, pairOfOption, edits)
            convertRotate(operationName, pairOfOption, edits)
        }
        return edits
    }

    private fun convertCrop(operationName: String, pairOfOption: Pair<String, List<String>>, edits: MutableList<OperationOption>) {
        if (operationName.equal(EditingUploadResources.Crop::class)) {
            val optionName = pairOfOption.first
            val values = pairOfOption.second
            if (optionName.equal(EditingUploadResources.Crop.Coordinate::class)) {
                edits.add(
                    EditingUploadResources.Crop.Coordinate(
                        values[0].toInt(),
                        values[1].toInt(),
                        values[2].toInt(),
                        values[3].toInt()
                    )
                )
            }

            if (optionName.equal(EditingUploadResources.Crop.PointScale::class)) {
                edits.add(
                    EditingUploadResources.Crop.PointScale(
                        values[0].toInt(),
                        values[1].toInt(),
                        values[2].toInt(),
                        values[3].toInt()
                    )
                )
            }

            if (operationName.equal(EditingUploadResources.Crop.Position::class)) {
                edits.add(
                    EditingUploadResources.Crop.Position(
                        CropOption.Position.valueOf(values[0]),
                        values[1].toInt(),
                        values[2].toInt()
                    )
                )
            }
        }
    }

    private fun convertResize(operationName: String, pairOfOption: Pair<String, List<String>>, edits: MutableList<OperationOption>) {
        if (operationName.equal(EditingUploadResources.Resize::class)) {
            val optionName = pairOfOption.first
            val values = pairOfOption.second

            if (optionName.equal(EditingUploadResources.Resize.Scale::class)) {
                edits.add(
                    EditingUploadResources.Resize.Scale(
                        values[0].toDouble()
                    )
                )
            }

            if (optionName.equal(EditingUploadResources.Resize.Size::class)) {
                edits.add(
                    EditingUploadResources.Resize.Size(
                        values[0].toInt(),
                        values[1].toInt(),
                        values[2].toBoolean()
                    )
                )
            }
        }
    }

    private fun convertRotate(operationName: String, pairOfOption: Pair<String, List<String>>, edits: MutableList<OperationOption>) {
        if (operationName.equal(EditingUploadResources.Rotate::class)) {
            val optionName = pairOfOption.first
            val values = pairOfOption.second

            if (optionName.equal(EditingUploadResources.Rotate.Angle::class)) {
                edits.add(
                    EditingUploadResources.Rotate.Angle(
                        values[0].toDouble()
                    )
                )
            }

            if (optionName.equal(EditingUploadResources.Rotate.Flip::class)) {
                edits.add(
                    EditingUploadResources.Rotate.Flip(
                        RotateOption.Flip.valueOf(values[0])
                    )
                )
            }
        }
    }

    private fun split(value: String): Pair<String, List<String>> {
        val splitOfOption = value.split(':')
        val option = splitOfOption[0]
        val values = splitOfOption[1].split(',')
        return Pair(option, values)
    }

    private fun String.equal(clazz: KClass<*>): Boolean {
        return (this == clazz.simpleName!!.decapitalize())
    }
}
