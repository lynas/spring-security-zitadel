package com.reev.authenticationserver.config.monolith

import com.reev.authenticationserver.config.zitadel.AccessTokenInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate


@Configuration
internal class ClientWebConfig {
    @Autowired
    private lateinit var tokenAccessor: TokenAccessor

    @Bean
    @Qualifier("zitadel")
    fun restTemplate(): RestTemplate {
        return RestTemplateBuilder()
            .interceptors(AccessTokenInterceptor(tokenAccessor))
            .build()
    }
}
