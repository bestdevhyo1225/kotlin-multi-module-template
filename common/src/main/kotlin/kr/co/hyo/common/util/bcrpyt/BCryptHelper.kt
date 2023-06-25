package kr.co.hyo.common.util.bcrpyt

import org.mindrot.jbcrypt.BCrypt

object BCryptHelper {

    fun encrypt(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())

    fun isMatch(password: String, encryptedPassword: String): Boolean = BCrypt.checkpw(password, encryptedPassword)

    fun isNotMatch(password: String, encryptedPassword: String): Boolean =
        !isMatch(password = password, encryptedPassword = encryptedPassword)
}
