package nonabili.ssoserver.config

import nonabili.ssoserver.service.CustomOauth2UserService
import nonabili.ssoserver.handler.OauthSuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(val customOauth2UserService: CustomOauth2UserService, val oauthSuccessHandler: OauthSuccessHandler) {
    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        return http
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .cors { it.disable() }
            .authorizeHttpRequests {
                it
                    .anyRequest().permitAll()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .oauth2Login {
                it
                    .successHandler(oauthSuccessHandler)
                    .authorizationEndpoint { it.baseUri("/sso/login") }
                    .userInfoEndpoint { it.userService(customOauth2UserService) }

            }
            .build()
    }
}