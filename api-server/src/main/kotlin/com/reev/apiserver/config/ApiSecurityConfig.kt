package com.reev.apiserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
internal class ApiSecurityConfig {
    @Bean
    fun securityChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity.sessionManagement() {
            it.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
            )
        }
        httpSecurity.authorizeRequests {
            it.mvcMatchers("/api/**").authenticated()
            it.anyRequest().authenticated() //
        }
//        httpSecurity.oauth2ResourceServer().opaqueToken()
        httpSecurity.oauth2ResourceServer().jwt()
        return httpSecurity.build()
    }
}
