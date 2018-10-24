package io.mustelidae.seaotter

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration

@SpringBootApplication(exclude = [EmbeddedMongoAutoConfiguration::class])
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
