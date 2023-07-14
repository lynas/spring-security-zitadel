package com.reev.apiserver.api

import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.oidc.StandardClaimNames
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
internal class PingController {
    @GetMapping("/api/ping/me")
    fun pingMe(auth: Authentication): Any {
        val tokenDetails = (auth as BearerTokenAuthentication).tokenAttributes
        val pingEcho = "Hello, " + tokenDetails[StandardClaimNames.PREFERRED_USERNAME] + " Ping successful."
        return mapOf(
            "ping_echo" to  pingEcho,
            "token_details" to tokenDetails
        )
    }
}
