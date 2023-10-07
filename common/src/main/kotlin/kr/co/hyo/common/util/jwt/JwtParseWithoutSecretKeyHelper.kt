package kr.co.hyo.common.util.jwt

import org.jose4j.jwt.consumer.InvalidJwtException
import org.jose4j.jwt.consumer.JwtConsumer
import org.jose4j.jwt.consumer.JwtConsumerBuilder

object JwtParseWithoutSecretKeyHelper {

    fun getValueFromClaims(accessToken: String, claimName: String): String {
        val jwtConsumer: JwtConsumer = JwtConsumerBuilder()
            .setSkipDefaultAudienceValidation()
            .setSkipSignatureVerification()
            .build()

        return try {
            jwtConsumer
                .processToClaims(accessToken)
                .getClaimValue(claimName)
                .toString()
        } catch (exception: InvalidJwtException) {
            throw IllegalArgumentException("UNAUTHORIZED (${exception.localizedMessage})")
        }
    }
}
