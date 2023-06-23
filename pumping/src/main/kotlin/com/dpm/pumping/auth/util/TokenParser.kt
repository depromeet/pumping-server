package com.dpm.pumping.auth.util


object TokenParser {
    private const val TOKEN_TYPE = "Bearer "

    fun extract(header: String?): String {
        header ?: throw IllegalArgumentException("header가 null입니다.")
        validateTokenType(header)
        return header.substring(TOKEN_TYPE.length)
    }

    private fun validateTokenType(header: String) {
        if (!header.startsWith(TOKEN_TYPE)) {
            throw IllegalArgumentException("Token이 Bearer타입이 아닙니다. : $header")
        }
    }
}
