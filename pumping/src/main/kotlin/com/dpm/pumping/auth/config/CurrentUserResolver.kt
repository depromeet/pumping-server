package com.dpm.pumping.auth.config

import com.dpm.pumping.user.domain.UserRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class CurrentUserResolver(
    private val userRepository: UserRepository
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginUser::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter, mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?
    ): Any {
        val request: HttpServletRequest =
            webRequest.getNativeRequest(HttpServletRequest::class.java)
                ?: throw IllegalArgumentException("request가 null입니다")
        val payload = request.getAttribute("PAYLOAD") ?: throw IllegalArgumentException("payload가 null입니다.")
        return userRepository.findById(payload as String)
            .orElseThrow { IllegalArgumentException("$payload 에 해당하는 유저를 찾을 수 없습니다") }
    }
}
