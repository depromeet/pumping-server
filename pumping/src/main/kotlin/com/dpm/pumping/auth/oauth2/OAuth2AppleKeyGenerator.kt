package com.dpm.pumping.auth.oauth2

import com.dpm.pumping.auth.oauth2.vo.OAuth2ApplePublicKey
import com.dpm.pumping.auth.oauth2.vo.OAuth2ApplePublicKeys
import java.math.BigInteger
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.RSAPublicKeySpec
import java.util.*


object OAuth2AppleKeyGenerator {

    private const val ALG_HEADER_KEY = "alg"
    private const val KID_HEADER_KEY = "kid"
    private const val POSITIVE_NUM = 1

    fun generatePublicKey(tokenHeaders: Map<String, String>, applePublicKeys: OAuth2ApplePublicKeys): PublicKey {
        val publicKey = applePublicKeys.getMatchesKey(tokenHeaders[ALG_HEADER_KEY], tokenHeaders[KID_HEADER_KEY])
        return generatePublicKeyWithApplePublicKey(publicKey)
    }

    private fun generatePublicKeyWithApplePublicKey(applePublicKey: OAuth2ApplePublicKey): PublicKey {

        val n = Base64.getDecoder().decode(applePublicKey.n)
        val e = Base64.getDecoder().decode(applePublicKey.e)
        val publicKeySpec = RSAPublicKeySpec(BigInteger(POSITIVE_NUM, n), BigInteger(POSITIVE_NUM, e))
        return try {
            val keyFactory: KeyFactory = KeyFactory.getInstance(applePublicKey.kty)
            keyFactory.generatePublic(publicKeySpec)
        } catch (exception: NoSuchAlgorithmException) {
            throw IllegalArgumentException("응답 받은 Apple Public Key로 PublicKey를 생성할 수 없습니다.")
        } catch (exception: InvalidKeySpecException) {
            throw IllegalArgumentException("응답 받은 Apple Public Key로 PublicKey를 생성할 수 없습니다.")
        }
    }
}
