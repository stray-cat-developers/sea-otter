package io.mustelidae.seaotter.api.resources

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class EditingStepValueDeserializerTest {

    @Test
    fun deserialize() {
        // Given
        val map = mapOf(
            "1:crop" to "coordinate:1,2,3,4",
            "2:resize" to "size:100,100,true",
            "3:rotate" to "angle:90.0"
        )
        val editingStepValueDeserializer = EditingStepValueDeserializer()
        // When
        val operationOptions = editingStepValueDeserializer.deserialize(map)
        // Then
        operationOptions.size shouldBe 3
        (operationOptions.first() is EditingUploadResources.Crop.Coordinate) shouldBe true
        (operationOptions[2] is EditingUploadResources.Rotate.Angle) shouldBe true
    }
}
