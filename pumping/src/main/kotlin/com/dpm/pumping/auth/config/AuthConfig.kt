package com.dpm.pumping.auth.config

import com.dpm.pumping.auth.application.JwtTokenProvider
import com.dpm.pumping.user.domain.UserRepository
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AuthConfig(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(AuthInterceptor(jwtTokenProvider))
            .addPathPatterns("/**")
            .excludePathPatterns("/docs/**", "/health", "/error")
            .excludePathPatterns("/api/v1/oauth2/**", "/api/v1/sign-up")
            // 빠른 기능 개발을 위한 임시 조치
            .excludePathPatterns("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**")
            .excludePathPatterns("/bypass/**")
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(CurrentUserResolver(userRepository))
    }
}
