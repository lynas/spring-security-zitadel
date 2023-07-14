package com.reev.authenticationserver.controller

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DemoRestController {
    @GetMapping("/hello")
    fun home(auth: Authentication): String {
        return "Hello ${auth.principal}"
    }
}