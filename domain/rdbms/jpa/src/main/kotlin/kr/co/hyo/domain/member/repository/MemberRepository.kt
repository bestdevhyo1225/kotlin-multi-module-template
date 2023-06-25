package kr.co.hyo.domain.member.repository

import kr.co.hyo.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>
