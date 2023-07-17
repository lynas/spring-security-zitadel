package com.reev.authenticationserver.config

import com.reev.authenticationserver.config.zitadel.ZitadelGrantedAuthoritiesMapper
import com.reev.authenticationserver.config.zitadel.ZitadelLogoutHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter
import org.springframework.security.web.SecurityFilterChain


@Configuration
internal class SecurityConfig {
    @Autowired
    private lateinit var zitadelLogoutHandler: ZitadelLogoutHandler

    @Bean
    fun securityChain(
        httpSecurity: HttpSecurity,
        clientRegistrationRepository: ClientRegistrationRepository?
    ): SecurityFilterChain {
        httpSecurity.authorizeHttpRequests {
            it.requestMatchers(
                "/webjars/**",
                "/resources/**",
                "/css/**",
                "/",
            ).permitAll()
            it.anyRequest().fullyAuthenticated()
        }
        httpSecurity.oauth2Client {
            val oauth2AuthRequestResolver =
                DefaultOAuth2AuthorizationRequestResolver(
                    clientRegistrationRepository,
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
}