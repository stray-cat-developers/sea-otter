package io.mustelidae.seaotter.domain.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.commons.CommonsMultipartFile

@RestController()
@RequestMapping("/upload")
class UploadController {

    @PostMapping("/multipart")
    fun upload(@RequestPart file: CommonsMultipartFile) {
    }
}
