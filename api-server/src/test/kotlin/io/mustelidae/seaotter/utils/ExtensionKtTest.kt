package io.mustelidae.seaotter.utils

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile

internal class ExtensionKtTest {

    @Test
    fun isSupport() {
        // Given
        val multipartFile = MockMultipartFile("test", "/abcd/test.jpg", "", "".toByteArray())
        // When
        val support = multipartFile.isSupport()
        // Then
        assertThat(support).isTrue()
    }

    @Test
    fun isNotSupport() {
        // Given
        val multipartFile = MockMultipartFile("test", "/abcd/test.text", "", "".toByteArray())
        // When
        val support = multipartFile.isSupport()
        // Then
        assertThat(support).isFalse()
    }
}
