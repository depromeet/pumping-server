package com.dpm.pumping.common.exception

import com.dpm.pumping.auth.application.JwtTokenProvider
import org.slf4j.LoggerFactory
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import java.lang.reflect.Method

class CustomAsyncExceptionHandler : AsyncUncaughtExceptionHandler {

    private val log = LoggerFactory.getLogger(CustomAsyncExceptionHandler::class.java)
    override fun handleUncaughtException(ex: Throwable, method: Method, vararg params: Any?) {
        log.warn("Exception message:${ex.message}")
        log.warn("Method name:${method.name}")
        for (param in params) {
            log.warn("Parameter value:$param")
        }
    }

}
