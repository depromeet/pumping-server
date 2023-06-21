package com.dpm.pumping.auth.oauth2.vo

data class OAuth2ApplePublicKeys(
    val keys: List<OAuth2ApplePublicKey>

) {
    fun getMatchesKey(alg: String?, kid: String?): OAuth2ApplePublicKey {
        return keys.stream()
            .filter { k -> k.alg == alg && k.kid == kid }
            .findFirst()
            .orElseThrow { IllegalArgumentException("Apple JWT 값의 alg, kid 정보가 올바르지 않습니다.") }
    }
}
