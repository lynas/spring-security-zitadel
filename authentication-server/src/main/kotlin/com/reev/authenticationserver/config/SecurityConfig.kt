package com.reev.authenticationserver.config

import com.reev.authenticationserver.config.zitadel.ZitadelGrantedAuthoritiesMapper
import com.reev.authenticationserver.config.zitadel.ZitadelLogoutHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.web.SecurityFilterChain


@Configuration
internal class SecurityConfig {

    @Value("\${zitadel.clientId}")
    lateinit var clientId: String

    @Value("\${zitadel.clientSecret}")
    lateinit var clientSecret: String
    @Value("\${zitadel.issuerUri}")
    lateinit var issuerUri: String

    @Autowired
    private lateinit var zitadelLogoutHandler: ZitadelLogoutHandler

    @Bean
    fun securityChain(
        httpSecurity: HttpSecurity,
    ): SecurityFilterChain {
        httpSecurity.authorizeHttpRequests {
            it.requestMatchers(
                "/webjars/**",
                "/resources/**",
                "/css/**",
                "/login/**",
                "/",
            ).permitAll()
            it.anyRequest().fullyAuthenticated()
        }
        httpSecurity.oauth2Client {
            val oauth2AuthRequestResolver =
                DefaultOAuth2AuthorizationRequestResolver(
                    clientRegistrationRepository(),
                    OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI //
                )
            it.authorizationCodeGrant{ authCodeGrant ->
                authCodeGrant.authorizationRequestResolver(oauth2AuthRequestResolver)
            }
        }
        httpSecurity.oauth2Login {
            it.userInfoEndpoint{userInfoEndpoint ->
                userInfoEndpoint.userAuthoritiesMapper(zitadelMapper())
            }
        }
        httpSecurity.logout {
            it.addLogoutHandler(zitadelLogoutHandler)
        }
        return httpSecurity.build()
    }

    private fun zitadelMapper(): GrantedAuthoritiesMapper {
        return ZitadelGrantedAuthoritiesMapper()
    }

    @Bean
    fun clientRegistrationRepository(): ClientRegistrationRepository {
        val registration = ClientRegistration.withRegistrationId("zitadel")
            .userNameAttributeName("preferred_username")
            .clientId(clientId)
            .clientSecret(clientSecret)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .scope("openid", "profile")
            .userNameAttributeName("preferred_username")
            .issuerUri(issuerUri)
            .redirectUri("{baseUrl}/login/oauth2/code/zitadel")
            .authorizationUri("$issuerUri/oauth/v2/authorize")
            .tokenUri("$issuerUri/oauth/v2/token")
            .jwkSetUri("$issuerUri/oauth/v2/keys")
            .userInfoUri("$issuerUri/oidc/v1/userinfo")
            .build()

        return InMemoryClientRegistrationRepository(registration)
    }
}
