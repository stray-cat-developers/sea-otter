package io.mustelidae.seaotter.api.controller

import io.mustelidae.seaotter.config.UnSupportException
import io.mustelidae.seaotter.utils.isSupport
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.commons.CommonsMultipartFile

@RestController()
@RequestMapping("/upload/simple")
class SimpleUploadController {

    @PostMapping("multipart", headers = ["content-type: multipart/form-data"])
    fun upload(@RequestPart multiPartFile: CommonsMultipartFile) {

        if(multiPartFile.isSupport().not())
            throw UnSupportException()
    }
}
