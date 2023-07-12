package com.dpm.pumping.common.exception

import io.jsonwebtoken.JwtException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.LocalDateTime


@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(IllegalArgumentException::class, IllegalStateException::class, JwtException::class)
    fun businessExceptionHandler(e: Exception?): ResponseEntity<Any>? {
        val currentRequestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        val requestUrl = currentRequestAttributes?.request!!.requestURI
        log.info("time:${LocalDateTime.now()},url:${requestUrl},message:${e}")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e!!.message)
    }
}
