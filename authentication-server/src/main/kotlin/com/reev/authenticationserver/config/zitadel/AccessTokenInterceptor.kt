package com.reev.authenticationserver.config.zitadel

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse


class AccessTokenInterceptor(
    private val tokenAccessor: TokenAccessor
) : ClientHttpRequestInterceptor {

    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        val grantedAccessToken = tokenAccessor.retrieveAccessToken()
        if (grantedAccessToken != null) {
            request.headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + grantedAccessToken.tokenValue)
        }
        return execution.execute(request, body)
    }
}
