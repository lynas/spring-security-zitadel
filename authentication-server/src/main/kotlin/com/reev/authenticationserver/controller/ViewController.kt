package com.reev.authenticationserver.controller

import com.reev.authenticationserver.config.monolith.TokenAccessor
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class ViewController (
    private val tokenAccessor: TokenAccessor
){

    @RequestMapping("/")
    fun home(): String {
        return "home"
    }

    @PostMapping("/private")
    fun private(model: Model, auth: Authentication): String {
        model.addAttribute("auth", auth.principal)
        return "private"
    }

    @GetMapping("/private")
    fun privateUI(model: Model, auth: Authentication): String {
        model.addAttribute("auth", auth.principal)
        model.addAttribute("token", tokenAccessor.retrieveAccessToken()?.tokenValue)
        return "private"
    }
}