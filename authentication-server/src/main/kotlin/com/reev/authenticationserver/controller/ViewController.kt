package com.reev.authenticationserver.controller

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class ViewController {

    @RequestMapping("/")
    fun home(): String {
        return "home"
    }

    @PostMapping("/private")
    fun private(model: Model, auth: Authentication): String {
        model.addAttribute("auth", auth.principal)
        return "private"
    }
}