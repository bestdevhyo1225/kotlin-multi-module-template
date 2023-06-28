package kr.co.hyo.domain.member.repository

import kr.co.hyo.domain.member.entity.MemberFollow
import org.springframework.data.jpa.repository.JpaRepository

interface MemberFollowJpaRepository : JpaRepository<MemberFollow, Long>
