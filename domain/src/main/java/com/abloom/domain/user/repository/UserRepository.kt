package com.abloom.domain.user.repository

import com.abloom.domain.user.model.Authentication
import com.abloom.domain.user.model.Sex
import com.abloom.domain.user.model.User
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface UserRepository {

    val loginUserId: String?

    /**
     * @return 가입하지 않은 회원일 경우 false 반환
     */
    suspend fun login(authentication: Authentication): Boolean

    /**
     * 회원가입 시 자동으로 로그인도 됩니다.
     */
    suspend fun join(
        authentication: Authentication,
        sex: Sex,
        marriageDate: LocalDate,
        name: String
    )

    fun getLoginUserFlow(): Flow<User?>

    fun getFianceFlow(): Flow<User?>

    suspend fun getUserByInvitationCode(invitationCode: String): User?

    suspend fun connectWith(fiance: User): Boolean

    suspend fun changeLoginUserName(name: String)

    suspend fun changeLoginUserMarriageDate(marriageDate: LocalDate)

    suspend fun logout()

    suspend fun leave()
}
