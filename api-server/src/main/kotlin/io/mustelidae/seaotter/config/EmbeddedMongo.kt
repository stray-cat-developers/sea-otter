package io.mustelidae.seaotter.config

import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component
import java.io.IOException

@Profile(value = ["default"])
@Import(value = [EmbeddedMongoAutoConfiguration::class])
@Component
class EmbeddedMongo {

    private val mongoHost = "localhost"
    private val mongoName = "sea-otter"
    @Bean
    @Throws(IOException::class)
    fun mongoTemplate(): MongoTemplate {
        val mongo = EmbeddedMongoFactoryBean()
        mongo.setBindIp(mongoHost)
        val mongoClient = mongo.`object`
        return MongoTemplate(mongoClient, mongoName)
    }
}
