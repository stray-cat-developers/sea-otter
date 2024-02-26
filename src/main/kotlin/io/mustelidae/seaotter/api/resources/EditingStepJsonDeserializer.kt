package io.mustelidae.seaotter.api.resources

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.mustelidae.seaotter.utils.Jackson
import java.io.IOException
import java.util.Locale
import kotlin.reflect.KClass

/**
 * Convert resource json to class for each state.
 * The class to convert must adhere to two rules.
 * 1. The edit step class is implemented under EditResources.
 * 2. edit json key name should be camelCase.
 */
class EditingStepJsonDeserializer : JsonDeserializer<List<OperationOption>>() {
    private val mapper = Jackson.getMapper()

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): List<OperationOption> {
        val edits: MutableList<OperationOption> = mutableListOf()

        for (nodeOfEdit in jp.codec.readTree<JsonNode>(jp)) {
            convertClass(
                mapper,
                edits,
                nodeOfEdit,
                EditingUploadResources.Crop::class,
                listOf(
                    EditingUploadResources.Crop.Position::class,
                    EditingUploadResources.Crop.Coordinate::class,
                    EditingUploadResources.Crop.PointScale::class,
                ),
            )

            convertClass(
                mapper,
                edits,
                nodeOfEdit,
                EditingUploadResources.Resize::class,
                listOf(
                    EditingUploadResources.Resize.Scale::class,
                    EditingUploadResources.Resize.Size::class,
                ),
            )

            convertClass(
                mapper,
                edits,
                nodeOfEdit,
                EditingUploadResources.Rotate::class,
                listOf(
                    EditingUploadResources.Rotate.Angle::class,
                    EditingUploadResources.Rotate.Flip::class,
                ),
            )
        }

        return edits
    }

    private fun convertClass(mapper: ObjectMapper, edits: MutableList<OperationOption>, nodeOfOperation: JsonNode, classOfOperation: KClass<*>, classesOfOperationOption: List<KClass<*>>) {
        val operationName = classOfOperation.simpleName!!.lowercase(Locale.getDefault())
        val mapOfOperationOption = classesOfOperationOption.map { it.simpleName!!.decapitalize() to it }

        if (nodeOfOperation.has(operationName)) {
            val nodeOfOperationOption = nodeOfOperation.get(operationName)

            for (pair in mapOfOperationOption) {
                val operationOptionName = pair.first

                if (nodeOfOperationOption.has(operationOptionName)) {
                    edits.add(mapper.convertValue(nodeOfOperationOption.get(operationOptionName), pair.second.java) as OperationOption)
                }
            }
        }
    }
}
