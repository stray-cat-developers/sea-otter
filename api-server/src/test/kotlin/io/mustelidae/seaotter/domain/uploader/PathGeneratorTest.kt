package io.mustelidae.seaotter.domain.uploader

import com.google.common.truth.Truth
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal class PathGeneratorTest {

    @Test
    fun getDateBasePath1() {
        // Given
        val path = "/apple"
        val date = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        // When
        val basePath = PathGenerator.getPathByDate(path)
        // Then
        Truth.assertThat("$date/apple").isEqualTo(basePath)
    }

    @Test
    fun getDateBasePath2() {
        // Given
        val path = "apple"
        val date = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        // When
        val basePath = PathGenerator.getPathByDate(path)
        // Then
        Truth.assertThat("$date/apple").isEqualTo(basePath)
    }
}
