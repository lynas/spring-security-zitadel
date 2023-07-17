package com.reev.authenticationserver.config.zitadel

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Component
import java.io.IOException


@Component
class ZitadelLogoutHandler : LogoutHandler {

    @Value("\${app.end-session.url}")
    private lateinit var endSessionEndpoint: String

    override fun logout(request: HttpServletRequest, response: HttpServletResponse, auth: Authentication) {
        val principalUser = auth.principal as DefaultOidcUser
        val idToken = principalUser.idToken
        val idTokenValue = idToken.tokenValue
        val redirectUri = generateUriFromRequest(request)
        val logoutUrl = createLogoutUrl(idTokenValue, redirectUri)
        try {
            //and then redirect to this URL after logging out
            response.sendRedirect(logoutUrl)
        } catch (e: IOException) {
            e.printStackTrace()
            //create your exception handling logic here
        }
    }

    private fun generateUriFromRequest(request: HttpServletRequest): String {
        var hostname = request.serverName + ":" + request.serverPort
        val isStandardHttps = "https" == request.scheme && request.serverPort == 443
        val isStandardHttp = "http" == request.scheme && request.serverPort == 80
        if (isStandardHttps || isStandardHttp) {
            hostname = request.serverName
        }
        return request.scheme + "://" + hostname + request.contextPath
    }

    private fun createLogoutUrl(idTokenValue: String, postRedirectUri: String): String {
        return ("$endSessionEndpoint?id_token_hint=$idTokenValue&post_logout_redirect_uri=$postRedirectUri")
    }

}
