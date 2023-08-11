package io.mustelidae.seaotter.utils

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile
import org.springframework.util.Base64Utils

internal class ExtensionKtTest {

    @Test
    fun isSupport() {
        // Given
        val multipartFile = MockMultipartFile("test", "/abcd/test.jpg", "", "".toByteArray())
        // When
        val support = multipartFile.isSupport()
        // Then
        support shouldBe true
    }

    @Test
    fun isNotSupport() {
        // Given
        val multipartFile = MockMultipartFile("test", "/abcd/test.text", "", "".toByteArray())
        // When
        val support = multipartFile.isSupport()
        // Then
        support shouldBe false
    }

    @Test
    fun base64SafeUrl() {
        val base64 = """
            data:image/jpeg;base64,/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAABAAEDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDu6KKKxGf/2Q==
        """.trimIndent()

        val encodedBase64SafeUrl = Base64Utils.encodeToUrlSafeString(base64.toByteArray())

        println(encodedBase64SafeUrl)

        val encodedBase64 = String(Base64Utils.decodeFromUrlSafeString(encodedBase64SafeUrl))

        println(encodedBase64)
    }
}
