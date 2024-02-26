package io.mustelidae.seaotter.api.controller

import io.mustelidae.seaotter.api.IntegrationTestSupport
import io.mustelidae.seaotter.common.Reply
import io.mustelidae.seaotter.utils.fromJson
import io.mustelidae.seaotter.utils.getTestFileAsAbsolutePath
import org.junit.jupiter.api.Test
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.io.File

class FileUploadControllerTest : IntegrationTestSupport() {

    private val topicCode = "507f1f77bcf86cd799439011"

    @Test
    fun upload() {
        val fileName = "테스트.numbers"
        val file = File(getTestFileAsAbsolutePath(fileName))
        val mockMultipartFile = MockMultipartFile("multiPartFile", fileName, null, file.inputStream())

        val url = linkTo<FileUploadController> { upload(mockMultipartFile, topicCode) }.toUri()

        val fileUrl = mockMvc.perform(
            MockMvcRequestBuilders.multipart(url)
                .file(mockMultipartFile)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
        ).andExpect(
            MockMvcResultMatchers.status().is2xxSuccessful
        ).andReturn()
            .response
            .contentAsString
            .fromJson<Reply<String>>()
            .content

        println(fileUrl)
    }
}
