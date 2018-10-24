package io.mustelidae.seaotter.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.mustelidae.seaotter.utils.Jackson
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.BufferedImageHttpMessageConverter
import org.springframework.http.converter.ByteArrayHttpMessageConverter
import org.springframework.http.converter.FormHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.ResourceHttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
class WebConfig : WebMvcConfigurerAdapter() {
    @Bean
    fun objectMapper(): ObjectMapper {
        return Jackson.getMapper()
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(BufferedImageHttpMessageConverter())
        converters.add(FormHttpMessageConverter())
        converters.add(MappingJackson2HttpMessageConverter())
        converters.add(StringHttpMessageConverter())
        converters.add(ByteArrayHttpMessageConverter())
        converters.add(ResourceHttpMessageConverter())
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
                .allowCredentials(true)
    }
}
