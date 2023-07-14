package com.reev.authenticationserver.config.zitadel

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority


class ZitadelGrantedAuthoritiesMapper : GrantedAuthoritiesMapper {
    override fun mapAuthorities(grantedAuthorities: Collection<GrantedAuthority>): Collection<GrantedAuthority> {
        val mappedAuthorities = HashSet<GrantedAuthority>()
        grantedAuthorities.forEach {
            if (it is SimpleGrantedAuthority) { //standard scopes
                mappedAuthorities.add(it)
            } else if (it is OidcUserAuthority) { //reserved scopes
                mapFromUserInfo(mappedAuthorities, it)
            }
        }
        return mappedAuthorities
    }

    private fun mapFromUserInfo(mappedAuthorities: HashSet<GrantedAuthority>, oidcUserAuthority: OidcUserAuthority) {
        val userInfo = oidcUserAuthority.userInfo
        val roleInfo = userInfo.getClaimAsMap(ZITADEL_ROLES_CLAIM)
        if (roleInfo.isNullOrEmpty()) {
            return
        }
        roleInfo.keys.forEach{
            mappedAuthorities.add(
                SimpleGrantedAuthority("ROLE_$it")
            )
        }
    }

    companion object {
        const val ZITADEL_ROLES_CLAIM = "urn:zitadel:iam:org:project:roles"
    }
}

