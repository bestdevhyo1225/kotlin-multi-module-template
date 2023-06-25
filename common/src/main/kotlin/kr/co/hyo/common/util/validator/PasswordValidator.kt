package kr.co.hyo.common.util.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class PasswordValidator : ConstraintValidator<Password, String> {

    companion object {
        private const val MIN_SIZE: Int = 10
        private const val MAX_SIZE: Int = 30
        private const val REGEX_PASSWORD: String =
            "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{${MIN_SIZE},${MAX_SIZE}}$"
    }

    override fun isValid(password: String, context: ConstraintValidatorContext): Boolean {
        val isValidPassword: Boolean = password.matches(REGEX_PASSWORD.toRegex())
        if (!isValidPassword) {
            val message = "${MIN_SIZE}자 이상의 ${MAX_SIZE}자 이하의 숫자, 영문자, 특수문자를 포함한 비밀번호를 입력해주세요"
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation()
        }
        return isValidPassword
    }
}
