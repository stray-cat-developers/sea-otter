package io.mustelidae.seaotter.api

import io.mustelidae.seaotter.Application
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest(classes = [(Application::class)], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class IntegrationTestSupport {

    @Autowired
    lateinit var mockMvc: MockMvc
}
