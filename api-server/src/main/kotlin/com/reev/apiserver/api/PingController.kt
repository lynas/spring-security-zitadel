package com.reev.apiserver.api

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
internal class PingController {
    @GetMapping("/api/ping/me")
    fun pingMe(auth: Authentication): Any {
        return mapOf(
            "auth_info" to auth.principal
        )
    }
}
