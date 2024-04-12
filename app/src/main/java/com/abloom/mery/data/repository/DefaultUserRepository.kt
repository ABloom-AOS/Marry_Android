package com.abloom.mery.data.repository

import com.abloom.domain.user.model.Authentication
import com.abloom.domain.user.model.Sex
import com.abloom.domain.user.model.User
import com.abloom.domain.user.repository.UserRepository
import com.abloom.mery.data.datastore.MeryPreferencesDataSource
import com.abloom.mery.data.di.ApplicationScope
import com.abloom.mery.data.firebase.user.UserDocument
import com.abloom.mery.data.firebase.user.UserFirebaseDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    private val preferencesDataSource: MeryPreferencesDataSource,
    private val firebaseDataSource: UserFirebaseDataSource
) : UserRepository {

    override val loginUserId: String?
        get() = firebaseDataSource.loginUserId

    override suspend fun login(
        authentication: Authentication
    ): Boolean = externalScope.async {
        val loginUser = when (authentication) {
            is Authentication.Google -> firebaseDataSource.loginByGoogle(authentication.token)
            is Authentication.Kakao -> firebaseDataSource.loginByEmail(
                email = authentication.email,
                password = authentication.password
            )
        } ?: return@async false

        firebaseDataSource.loginUpdateFcmToken(loginUser.uid)
        val isJoined = firebaseDataSource.isExist(loginUser.uid)
        if (isJoined) preferencesDataSource.updateLoginUserId(loginUser.uid)
        return@async isJoined
    }.await()

    override suspend fun join(
        authentication: Authentication,
        sex: Sex,
        marriageDate: LocalDate,
        name: String
    ) = externalScope.launch {
        val joinedUser = when (authentication) {
            is Authentication.Google -> firebaseDataSource.loginByGoogle(authentication.token)
            is Authentication.Kakao -> firebaseDataSource.signUpByEmail(
                email = authentication.email,
                password = authentication.password
            )
        } ?: return@launch

        val userDocument = UserDocument.create(
            id = joinedUser.uid,
            name = name,
            marriageDate = marriageDate,
            sex = sex,
            invitationCode = createInvitationCodeFrom(joinedUser.uid)
        )

        firebaseDataSource.createUserDocument(userDocument)
        firebaseDataSource.loginUpdateFcmToken(joinedUser.uid)
        preferencesDataSource.updateLoginUserId(joinedUser.uid)
    }.join()

    private fun createInvitationCodeFrom(id: String): String = id.toList()
        .shuffled()
        .take(INVITATION_CODE_LENGTH)
        .joinToString(separator = "")

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getLoginUserFlow(): Flow<User?> = preferencesDataSource.loginUserId
        .flatMapLatest { loginUserId ->
            if (loginUserId == null) return@flatMapLatest flowOf(null)
            check(firebaseDataSource.loginUserId == loginUserId) { "firebase의 유저 아이디와 로컬 데이터의 유저 아이디가 다릅니다." }
            firebaseDataSource.getUserDocumentFlow(loginUserId)
                .map { userDocument -> userDocument?.asExternal() }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFianceFlow(): Flow<User?> = getLoginUserFlow().flatMapLatest { loginUser ->
        if (loginUser == null) return@flatMapLatest flowOf(null)
        val fianceId = loginUser.fianceId ?: return@flatMapLatest flowOf(null)
        firebaseDataSource.getUserDocumentFlow(fianceId)
            .map { userDocument -> userDocument?.asExternal() }
    }

    override suspend fun getUserByInvitationCode(
        invitationCode: String
    ): User? = withContext(Dispatchers.IO) {
        firebaseDataSource.getUserDocumentByInvitationCode(invitationCode)
            ?.let(UserDocument::asExternal)
    }

    override suspend fun connectWith(
        fiance: User
    ) = externalScope.async {
        firebaseDataSource.connect(loginUserId ?: return@async, fiance.id)
    }.await()

    override suspend fun changeLoginUserName(name: String) = externalScope.launch {
        val loginUserId = firebaseDataSource.loginUserId ?: return@launch
        firebaseDataSource.updateName(loginUserId, name)
    }.join()

    override suspend fun changeLoginUserMarriageDate(
        marriageDate: LocalDate
    ) = externalScope.launch {
        val loginUserId = firebaseDataSource.loginUserId ?: return@launch
        firebaseDataSource.updateMarriageDate(loginUserId, marriageDate)
    }.join()

    override suspend fun logout() = externalScope.launch {
        val loginUserId = firebaseDataSource.loginUserId ?: return@launch
        preferencesDataSource.removeLoginUserId()
        firebaseDataSource.signOut()
        firebaseDataSource.logOutUpdateFcmToken(loginUserId)
    }.join()

    override suspend fun leave() = externalScope.launch {
        val loginUserId = firebaseDataSource.loginUserId ?: return@launch
        firebaseDataSource.delete(loginUserId)
        preferencesDataSource.removeLoginUserId()
    }.join()

    companion object {

        private const val INVITATION_CODE_LENGTH = 10
    }
}
