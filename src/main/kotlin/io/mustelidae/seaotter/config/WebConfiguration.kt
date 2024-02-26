package io.mustelidae.seaotter.config

import com.twelvemonkeys.servlet.image.IIOProviderContextListener
import io.mustelidae.seaotter.utils.Jackson
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar
import org.springframework.format.support.FormattingConversionService
import org.springframework.http.converter.BufferedImageHttpMessageConverter
import org.springframework.http.converter.ByteArrayHttpMessageConverter
import org.springframework.http.converter.FormHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.ResourceHttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration
import java.time.format.DateTimeFormatter

@Configuration
@ControllerAdvice
class WebConfiguration : DelegatingWebMvcConfiguration() {
    @Bean
    fun iIOProviderContextListener(): IIOProviderContextListener = IIOProviderContextListener()

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        val objectMapper = Jackson.getMapper()
        converters.add(ByteArrayHttpMessageConverter())
        converters.add(StringHttpMessageConverter())
        converters.add(MappingJackson2HttpMessageConverter(objectMapper))
        converters.add(BufferedImageHttpMessageConverter())
        converters.add(FormHttpMessageConverter())
        converters.add(ResourceHttpMessageConverter())
    }

    @Bean
    override fun mvcConversionService(): FormattingConversionService {
        val conversionService = super.mvcConversionService()
        val dateTimeRegistrar = DateTimeFormatterRegistrar()
        dateTimeRegistrar.setDateFormatter(DateTimeFormatter.ISO_DATE)
        dateTimeRegistrar.setTimeFormatter(DateTimeFormatter.ISO_TIME)
        dateTimeRegistrar.setDateTimeFormatter(DateTimeFormatter.ISO_DATE_TIME)
        dateTimeRegistrar.registerFormatters(conversionService)
        return conversionService
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("*")
            .allowCredentials(false)
            .maxAge(3600)
    }
}
