package com.reev.authenticationserver.controller

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject


@RestController
@RequestMapping("/api")
internal class ExternalApiCallController(
    @Qualifier("zitadel")
    private val restTemplate: RestTemplate
) {


    @GetMapping("/pings/me")
    fun pingServer(auth: Authentication?): Any {
        return restTemplate.getForObject<Map<*, *>>(
            "http://localhost:18090/api/ping/me",
            MutableMap::class.java
        )
    }

    @GetMapping("/reev")
    fun reevBE(auth: Authentication?): String? {
        return restTemplate.getForObject(
            "http://localhost:9000/api/sample",
            String::class.java
        )
    }
}
