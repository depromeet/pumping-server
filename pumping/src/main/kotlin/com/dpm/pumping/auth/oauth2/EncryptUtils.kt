package com.dpm.pumping.auth.oauth2

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object EncryptUtils {

    private const val ALGORITHM = "SHA-256"
    fun encrypt(value: String): String {
        return try {
            val sha256: MessageDigest = MessageDigest.getInstance(ALGORITHM)
            val digest: ByteArray = sha256.digest(value.toByteArray(StandardCharsets.UTF_8))
            val hexString = StringBuilder()
            for (b in digest) {
                hexString.append(String.format("%02x", b))
            }
            hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalArgumentException("Apple OAuth 통신 암호화 과정 중 문제가 발생했습니다.")
        }
    }
}
