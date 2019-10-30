package io.mustelidae.seaotter.api.controller

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.core.Method
import com.google.common.truth.Truth.assertThat
import io.mustelidae.seaotter.api.IntegrationTestSupport
import io.mustelidae.seaotter.api.resources.EditingUploadResources
import io.mustelidae.seaotter.utils.fromJsonByContent
import io.mustelidae.seaotter.utils.getTestImageFileAsAbsolutePath
import io.mustelidae.seaotter.utils.success
import org.junit.jupiter.api.Test
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.mock.web.MockMultipartFile
import java.io.File

internal class EditingImageUploadControllerTest : IntegrationTestSupport() {

    @Test
    fun uploadMultipart() {
        // Given
        val fileName = "snapshot.png"
        val file = File(getTestImageFileAsAbsolutePath(fileName))
        val parameters = listOf(
            "1:crop" to "coordinate:0,0,100,100",
            "2:resize" to "scale:50.0",
            "3:rotate" to "flip:HORZ",
            "4:rotate" to "angle:180.0"
        )
        val url = "http://localhost:$port" + linkTo(methodOn(EditingImageUploadController::class.java).upload(MockMultipartFile(file.name, file.inputStream()), mapOf(), false)).toUri().path
        // When
        val replies = Fuel.upload(url, Method.POST, parameters)
            .add(FileDataPart(file, "multiPartFile", fileName))
            .responseString()
            .success()
            .fromJsonByContent<List<EditingUploadResources.ReplyOnImage>>()
        // Then
        assertThat(replies[0].width).isEqualTo(50)
        assertThat(replies[0].height).isEqualTo(50)
        assertThat(replies[0].histories).isNotEmpty()
        assertThat(replies[0].histories?.get(0)).isEqualTo("crop")
    }

    @Test
    fun upload1() {
        // Given
        // When

        // Then
    }

    @Test
    fun upload2() {
        // Given
        // When

        // Then
    }
}
