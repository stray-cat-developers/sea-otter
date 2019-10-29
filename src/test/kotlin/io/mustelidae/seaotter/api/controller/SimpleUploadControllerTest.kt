package io.mustelidae.seaotter.api.controller

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.util.encodeBase64UrlToString
import com.google.common.truth.Truth.assertThat
import io.mustelidae.seaotter.api.IntegrationTestSupport
import io.mustelidae.seaotter.api.resources.UploadResources
import io.mustelidae.seaotter.domain.delivery.Image
import io.mustelidae.seaotter.utils.fromJsonByContent
import io.mustelidae.seaotter.utils.getTestImageFileAsAbsolutePath
import io.mustelidae.seaotter.utils.success
import io.mustelidae.seaotter.utils.toJson
import org.junit.jupiter.api.Test
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.mock.web.MockMultipartFile
import org.springframework.util.Base64Utils
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

internal class SimpleUploadControllerTest : IntegrationTestSupport() {

    @Test
    fun uploadMultipart() {
        // Given
        val fileName = "snapshot.png"
        val file = File(getTestImageFileAsAbsolutePath(fileName))
        val bufferedImage = Image.from(file).bufferedImage
        val url = "http://localhost:$port" + linkTo(methodOn(SimpleUploadController::class.java).upload(MockMultipartFile(file.name, file.inputStream()), false)).toUri().path

        // When
        val replies = Fuel.upload(url, Method.POST)
            .add(FileDataPart(file, "multiPartFile", fileName))
            .responseString()
            .success()
            .fromJsonByContent<List<UploadResources.ReplyOnImage>>()
        // Then
        assertThat(replies[0].width).isEqualTo(bufferedImage.width)
        assertThat(replies[0].height).isEqualTo(bufferedImage.height)
    }

    @Test
    fun uploadBase64Form() {
        // Given
        val fileName = "snapshot.png"
        val file = File(getTestImageFileAsAbsolutePath(fileName))
        val bufferedImage = Image.from(file).bufferedImage

        val out = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "PNG", out)
        val base64 = "data:image/png;base64," + Base64Utils.encodeToString(out.toByteArray())
        val url = "http://localhost:$port" + linkTo(methodOn(SimpleUploadController::class.java).upload("", false)).toUri().path
        val parameters = listOf(
            Pair("base64", base64),
            Pair("hasOriginal", "false")
        )
        // When
        val replies = Fuel.post(url, parameters)
            .header(mapOf("Content-type" to "application/x-www-form-urlencoded"))
            .responseString()
            .success()
            .fromJsonByContent<List<UploadResources.ReplyOnImage>>()
        // Then
        assertThat(replies[0].width).isEqualTo(bufferedImage.width)
        assertThat(replies[0].height).isEqualTo(bufferedImage.height)
    }

    @Test
    fun uploadJson() {
        // Given
        val fileName = "snapshot.png"
        val file = File(getTestImageFileAsAbsolutePath(fileName))
        val bufferedImage = Image.from(file).bufferedImage

        val out = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "PNG", out)
        val base64 = "data:image/png;base64," + Base64Utils.encodeToString(out.toByteArray())
        val base64SafeUrl = base64.encodeBase64UrlToString()

        val request = UploadResources.Request(
            base64SafeUrl,
            false
        )

        val url = "http://localhost:$port" + linkTo(methodOn(SimpleUploadController::class.java).upload(request)).toUri().path
        // When
        val replies = Fuel.post(url)
            .header(Pair("Content-type", "application/json"))
            .jsonBody(request.toJson())
            .responseString()
            .success()
            .fromJsonByContent<List<UploadResources.ReplyOnImage>>()
        // Then
        assertThat(replies[0].width).isEqualTo(bufferedImage.width)
        assertThat(replies[0].height).isEqualTo(bufferedImage.height)
    }
}
