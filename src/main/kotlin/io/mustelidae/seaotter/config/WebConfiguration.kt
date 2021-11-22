package io.mustelidae.seaotter.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.twelvemonkeys.servlet.image.IIOProviderContextListener
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
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import org.springframework.web.servlet.view.InternalResourceViewResolver

@Configuration
class WebConfiguration : WebMvcConfigurationSupport() {
    @Bean
    fun objectMapper(): ObjectMapper {
        return Jackson.getMapper()
    }

    @Bean
    fun iIOProviderContextListener(): IIOProviderContextListener = IIOProviderContextListener()

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(BufferedImageHttpMessageConverter())
        converters.add(FormHttpMessageConverter())
        converters.add(MappingJackson2HttpMessageConverter(objectMapper()) as HttpMessageConverter<*>)
        converters.add(StringHttpMessageConverter())
        converters.add(ByteArrayHttpMessageConverter())
        converters.add(ResourceHttpMessageConverter())
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("*")
            .allowCredentials(false)
            .maxAge(3600)
    }

    override fun configureViewResolvers(registry: ViewResolverRegistry) {
        val resolver = InternalResourceViewResolver()
        resolver.setPrefix("/WEB-INF/")
        resolver.setSuffix(".html")
        registry.viewResolver(resolver)

        super.configureViewResolvers(registry)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/")
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
    }
}
