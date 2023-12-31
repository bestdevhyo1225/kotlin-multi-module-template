package kr.co.hyo.api.member.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.co.hyo.api.member.request.MemberChangeEmailRequest
import kr.co.hyo.api.member.request.MemberChangePasswordRequest
import kr.co.hyo.api.member.request.MemberFollowRequest
import kr.co.hyo.api.member.request.MemberRefreshTokenRequest
import kr.co.hyo.api.member.request.MemberSignInRequest
import kr.co.hyo.api.member.request.MemberSignUpRequest
import kr.co.hyo.api.member.service.MemberSignInService
import kr.co.hyo.api.member.service.MemberSignOutService
import kr.co.hyo.common.util.auth.Auth
import kr.co.hyo.common.util.auth.AuthInfo
import kr.co.hyo.domain.member.dto.MemberDto
import kr.co.hyo.domain.member.dto.MemberFollowDto
import kr.co.hyo.domain.member.service.MemberFollowWriteService
import kr.co.hyo.domain.member.service.MemberReadService
import kr.co.hyo.domain.member.service.MemberWriteService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/members")
@Tag(name = "회원", description = "API Document")
class MemberController(
    private val memberReadService: MemberReadService,
    private val memberWriteService: MemberWriteService,
    private val memberFollowWriteService: MemberFollowWriteService,
    private val memberSignInService: MemberSignInService,
    private val memberSignOutService: MemberSignOutService,
) {

    @PostMapping("/sign-up")
    @ResponseStatus(value = CREATED)
    @Operation(description = "회원 가입")
    @SecurityRequirements
    fun signUp(@Valid @RequestBody request: MemberSignUpRequest): ResponseEntity<MemberDto> {
        val dto: MemberDto = memberWriteService.createMember(dto = request.toDto())
        return ResponseEntity
            .created(URI.create("/members/" + dto.id))
            .body(dto)
    }

    @PostMapping("/sign-in")
    @Operation(description = "회원 로그인")
    @SecurityRequirements
    fun signIn(@Valid @RequestBody request: MemberSignInRequest): ResponseEntity<Map<String, Any>> {
        val data: Map<String, Any> = memberSignInService.verify(loginId = request.loginId, password = request.password)
        return ResponseEntity.ok(data)
    }

    @PostMapping("/refresh-token")
    @Operation(description = "회원 인증 토큰 재발급")
    fun refreshToken(
        @Auth @Parameter(hidden = true) authInfo: AuthInfo,
        @Valid @RequestBody request: MemberRefreshTokenRequest,
    ): ResponseEntity<Map<String, Any>> {
        val data: Map<String, Any> =
            memberSignInService.refresh(memberId = authInfo.memberId, refreshToken = request.refreshToken)
        return ResponseEntity.ok(data)
    }

    @PostMapping("/follow")
    @Operation(description = "회원 팔로우")
    fun follow(
        @Auth @Parameter(hidden = true) authInfo: AuthInfo,
        @Valid @RequestBody request: MemberFollowRequest,
    ): ResponseEntity<MemberFollowDto> {
        val dto: MemberFollowDto = memberFollowWriteService.createMemberFollow(
            followingId = request.followingId,
            followerId = authInfo.memberId,
        )
        return ResponseEntity.ok(dto)
    }

    @PatchMapping("/password")
    @Operation(description = "회원 비밀번호 변경")
    fun password(
        @Auth @Parameter(hidden = true) authInfo: AuthInfo,
        @Valid @RequestBody request: MemberChangePasswordRequest,
    ) {
        val (oldPassword: String, newPassword: String) = request
        memberWriteService.changeMemberPassword(
            memberId = authInfo.memberId,
            oldPassword = oldPassword,
            newPassword = newPassword,
        )
    }

    @PatchMapping("/email")
    @Operation(description = "회원 이메일 변경")
    fun email(
        @Auth @Parameter(hidden = true) authInfo: AuthInfo,
        @Valid @RequestBody request: MemberChangeEmailRequest,
    ) {
        memberWriteService.changeMemberEmail(memberId = authInfo.memberId, email = request.email)
    }

    @GetMapping("/me")
    @Operation(description = "회원 본인 정보 조회")
    fun me(@Auth @Parameter(hidden = true) authInfo: AuthInfo): ResponseEntity<MemberDto> {
        val dto: MemberDto = memberReadService.findMember(memberId = authInfo.memberId)
        return ResponseEntity.ok(dto)
    }

    @DeleteMapping("/sign-out")
    @Operation(description = "회원 탈퇴")
    fun signOut(@Auth @Parameter(hidden = true) authInfo: AuthInfo) {
        memberSignOutService.out(memberId = authInfo.memberId, accessToken = authInfo.accessToken)
    }
}
