package com.reev.authenticationserver.config.zitadel

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.stereotype.Component


@Component
class TokenAccessor {

    @Autowired
    private lateinit var oauth2ClientService: OAuth2AuthorizedClientService

    fun retrieveAccessToken(): OAuth2AccessToken? {
        return getAccessToken(SecurityContextHolder.getContext().authentication)
    }

    fun getAccessToken(clientAuth: Authentication): OAuth2AccessToken? {
        val authToken = clientAuth as OAuth2AuthenticationToken
        val clientId = authToken.authorizedClientRegistrationId
        val username = clientAuth.getName()
        val authorizedClient = oauth2ClientService.loadAuthorizedClient<OAuth2AuthorizedClient>(clientId, username)
        return authorizedClient?.accessToken
    }
}
