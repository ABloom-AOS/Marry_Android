package com.abloom.mery.data.repository

import com.abloom.domain.user.model.Authentication
import com.abloom.domain.user.model.Sex
import com.abloom.domain.user.model.User
import com.abloom.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeUserRepository @Inject constructor() : UserRepository {

    override suspend fun login(authentication: Authentication): Boolean {
        if (JOINED_USER == null) return false
        LOGIN_USER.value = JOINED_USER
        return true
    }

    override suspend fun join(
        authentication: Authentication,
        sex: Sex,
        marriageDate: LocalDate,
        name: String
    ) {
        JOINED_USER = User(
            id = "asdf",
            name = name,
            marriageDate = marriageDate,
            sex = sex,
            invitationCode = "asdfa",
            fianceId = null
        )
        LOGIN_USER.value = JOINED_USER
    }

    override fun getLoginUser(): Flow<User?> = LOGIN_USER.asStateFlow()

    override fun getFiance(): Flow<User?> = FIANCE.asStateFlow()

    override suspend fun connectWithFiance(fianceInvitationCode: String): Boolean {
        if (fianceInvitationCode != FIANCE_INVITATION_CODE) return false

        FIANCE.value = User(
            id = "asdfadf",
            name = "최지은",
            marriageDate = LOGIN_USER.value!!.marriageDate,
            sex = Sex.FEMALE,
            invitationCode = FIANCE_INVITATION_CODE,
            fianceId = LOGIN_USER.value!!.fianceId
        )
        return true
    }

    override suspend fun changeLoginUserName(name: String) {
        LOGIN_USER.value = LOGIN_USER.value!!.copy(name = name)
    }

    override suspend fun changeLoginUserMarriageDate(marriageDate: LocalDate) {
        LOGIN_USER.value = LOGIN_USER.value!!.copy(marriageDate = marriageDate)
    }

    override suspend fun logout() {
        LOGIN_USER.value = null
    }

    override suspend fun leave() {
        JOINED_USER = null
        LOGIN_USER.value = null
    }

    companion object {

        const val FIANCE_INVITATION_CODE: String = "asdf"

        private val DUMMY_USER = User(
            id = "asdf",
            name = "이지훈",
            marriageDate = LocalDate.of(2024, 3, 27),
            sex = Sex.MALE,
            invitationCode = "asdf",
            fianceId = "qwer"
        )

        private val DUMMY_FIANCE: User = User(
            id = "qwer",
            name = "최지은",
            marriageDate = LocalDate.now(),
            sex = Sex.FEMALE,
            invitationCode = "asdf",
            fianceId = "asdf"
        )

        var JOINED_USER: User? = DUMMY_USER
        var LOGIN_USER = MutableStateFlow<User?>(null)
        var FIANCE = MutableStateFlow<User?>(null)
    }
}
