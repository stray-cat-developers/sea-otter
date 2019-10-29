package io.mustelidae.seaotter.api

import io.mustelidae.seaotter.Application
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ComponentScan
@SpringBootTest(classes = [(Application::class)], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTestSupport {
    @LocalServerPort
    var port: Int = 0
}
