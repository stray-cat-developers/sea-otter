package io.mustelidae.seaotter.api.resources

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class EditingStepValueDeserializerTest {

    @Test
    fun deserialize() {
        // Given
        val map = mapOf(
            "crop" to "coordinate:1,2,3,4",
            "resize" to "size:100,100,true",
            "rotate" to "angle:90.0"
        )
        val editingStepValueDeserializer = EditingStepValueDeserializer()
        // When
        val operationOptions = editingStepValueDeserializer.deserialize(map)
        // Then
        assertThat(operationOptions.size).isEqualTo(3)
    }
}
