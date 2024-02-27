package io.mustelidae.seaotter.api.controller

import com.github.kittinunf.fuel.util.encodeBase64UrlToString
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import io.mustelidae.seaotter.api.IntegrationTestSupport
import io.mustelidae.seaotter.api.resources.EditingUploadResources
import io.mustelidae.seaotter.api.resources.UploadResources
import io.mustelidae.seaotter.common.Replies
import io.mustelidae.seaotter.domain.delivery.Image
import io.mustelidae.seaotter.utils.fromJson
import io.mustelidae.seaotter.utils.fromJsonByContent
import io.mustelidae.seaotter.utils.getTestImageFileAsAbsolutePath
import io.mustelidae.seaotter.utils.toJson
import org.junit.jupiter.api.Test
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Base64
import javax.imageio.ImageIO

internal class SimpleUploadControllerTest : IntegrationTestSupport() {

    private val topicCode = "507f191e810c19729de860ea"

    @Test
    fun uploadMultipart() {
        // Given
        val fileName = "snapshot.png"
        val file = File(getTestImageFileAsAbsolutePath(fileName))
        val bufferedImage = Image.from(file).bufferedImage

        // When
        val uri = linkTo<SimpleUploadController> { upload(MockMultipartFile(file.name, file.inputStream()), false, topicCode) }.toUri()
        val replies = mockMvc.perform(
            MockMvcRequestBuilders.multipart(uri)
                .file(MockMultipartFile("multiPartFile", fileName, null, file.inputStream()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE),
        ).andExpect(
            status().is2xxSuccessful,
        ).andReturn()
            .response
            .contentAsString
            .fromJsonByContent<List<UploadResources.ReplyOnImage>>()
        // Then
        replies.first().asClue {
            it.width shouldBe bufferedImage.width
            it.height shouldBe bufferedImage.height
        }
    }

    @Test
    fun uploadBase64Form() {
        // Given
        val fileName = "snapshot.png"
        val file = File(getTestImageFileAsAbsolutePath(fileName))
        val bufferedImage = Image.from(file).bufferedImage

        val out = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "PNG", out)
        val base64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(out.toByteArray())
        val uri = linkTo<SimpleUploadController> { upload("", false, topicCode) }.toUri()
        val parameters = LinkedMultiValueMap<String, String>().apply {
            add("base64", base64)
            add("hasOriginal", "false")
        }
        // When
        val replies = mockMvc.post(uri) {
            params = parameters
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn()
            .response
            .contentAsString
            .fromJsonByContent<List<UploadResources.ReplyOnImage>>()
        // Then
        replies.first().asClue {
            it.width shouldBe bufferedImage.width
            it.height shouldBe bufferedImage.height
        }
    }

    @Test
    fun uploadJson() {
        // Given
        val fileName = "snapshot.png"
        val file = File(getTestImageFileAsAbsolutePath(fileName))
        val bufferedImage = Image.from(file).bufferedImage

        val out = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "PNG", out)
        val base64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(out.toByteArray())
        val base64SafeUrl = base64.encodeBase64UrlToString()

        val request = UploadResources.Request(
            base64SafeUrl,
            false,
        )

        // When
        val replies = mockMvc.post(linkTo<SimpleUploadController> { upload(request) }.toUri()) {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = request.toJson()
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn()
            .response
            .contentAsString
            .fromJson<Replies<EditingUploadResources.ReplyOnImage>>()
            .getContent()
        // Then
        replies.first().asClue {
            it.width shouldBe bufferedImage.width
            it.height shouldBe bufferedImage.height
        }
    }

    @Test
    fun uploadUrl() {
        // Given
        val imageUrl = "https://t1.daumcdn.net/daumtop_chanel/op/20200723055344399.png"
        val request = UploadResources.RequestOnUrl(
            imageUrl,
            false,
        )

        val replies = mockMvc.post(linkTo<SimpleUploadController> { upload(request) }.toUri()) {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = request.toJson()
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn()
            .response
            .contentAsString
            .fromJson<Replies<EditingUploadResources.ReplyOnImage>>()
            .getContent()
        // Then
        replies.first().asClue {
            it.width shouldBe 360
            it.height shouldBe 188
        }
    }

    @Test
    fun uploadMultipartSupportWebp() {
        // Given
        val fileName = "denden_RGB_2784x1856.webp"
        val file = File(getTestImageFileAsAbsolutePath(fileName))
        val bufferedImage = Image.from(file).bufferedImage

        // When
        val uri = linkTo<SimpleUploadController> { upload(MockMultipartFile(file.name, file.inputStream()), false, topicCode) }.toUri()
        val replies = mockMvc.perform(
            MockMvcRequestBuilders.multipart(uri)
                .file(MockMultipartFile("multiPartFile", fileName, null, file.inputStream()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE),
        ).andExpect(
            status().is2xxSuccessful,
        ).andReturn()
            .response
            .contentAsString
            .fromJsonByContent<List<UploadResources.ReplyOnImage>>()
        // Then
        replies.first().asClue {
            it.width shouldBe bufferedImage.width
            it.height shouldBe bufferedImage.height
        }
    }

    @Test
    fun uploadMultipartSupportHdr() {
        // Given
        val fileName = "denden_RGB_2784x1856.hdr"
        val file = File(getTestImageFileAsAbsolutePath(fileName))
        val bufferedImage = Image.from(file).bufferedImage

        // When
        val uri = linkTo<SimpleUploadController> { upload(MockMultipartFile(file.name, file.inputStream()), false, topicCode) }.toUri()
        val replies = mockMvc.perform(
            MockMvcRequestBuilders.multipart(uri)
                .file(MockMultipartFile("multiPartFile", fileName, null, file.inputStream()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE),
        ).andExpect(
            status().is2xxSuccessful,
        ).andReturn()
            .response
            .contentAsString
            .fromJsonByContent<List<UploadResources.ReplyOnImage>>()
        // Then
        replies.first().asClue {
            it.width shouldBe bufferedImage.width
            it.height shouldBe bufferedImage.height
        }
    }
}
