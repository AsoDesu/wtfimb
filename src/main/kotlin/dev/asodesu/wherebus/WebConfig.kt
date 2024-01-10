package dev.asodesu.wherebus

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.view.InternalResourceViewResolver


@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**").allowedMethods("*")
    }

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/").setViewName("index")
        registry.addViewController("/map").setViewName("map")
    }

    @Bean
    fun internalResourceViewResolver(): InternalResourceViewResolver {
        val internalResourceViewResolver = InternalResourceViewResolver()
        internalResourceViewResolver.setSuffix(".html")
        return internalResourceViewResolver
    }


}