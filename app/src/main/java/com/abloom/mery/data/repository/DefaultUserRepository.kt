package com.abloom.mery.data.repository

import com.abloom.domain.user.model.Authentication
import com.abloom.domain.user.model.Sex
import com.abloom.domain.user.model.User
import com.abloom.domain.user.repository.UserRepository
import com.abloom.mery.data.datastore.MeryPreferencesDataSource
import com.abloom.mery.data.di.ApplicationScope
import com.abloom.mery.data.firebase.UserDocument
import com.abloom.mery.data.firebase.UserFirebaseDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.asDeferred
import java.time.LocalDate
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    private val preferencesDataSource: MeryPreferencesDataSource,
    private val userFirebaseDataSource: UserFirebaseDataSource
) : UserRepository {

    override suspend fun login(
        authentication: Authentication
    ): Boolean = externalScope.async {
        val loginUser = when (authentication) {
            is Authentication.Google -> userFirebaseDataSource.loginByGoogle(authentication.token)

            is Authentication.Kakao -> userFirebaseDataSource.loginByEmail(
                email = authentication.email,
                password = authentication.password
            )
        } ?: return@async false

        val isJoined = userFirebaseDataSource.isExist(loginUser.uid)
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
            is Authentication.Google -> userFirebaseDataSource.loginByGoogle(authentication.token)

            is Authentication.Kakao -> userFirebaseDataSource.signUpByEmail(
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

        userFirebaseDataSource.createUserDocument(userDocument)
        preferencesDataSource.updateLoginUserId(joinedUser.uid)
    }.join()

    private fun createInvitationCodeFrom(id: String): String = id.toList()
        .shuffled()
        .take(INVITATION_CODE_LENGTH)
        .joinToString(separator = "")

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getLoginUser(): Flow<User?> = preferencesDataSource.loginUserId
        .flatMapLatest { loginUserId ->
            if (loginUserId == null) return@flatMapLatest flow { emit(null) }
            userFirebaseDataSource.getUserDocumentFlow(loginUserId)
                .map { userDocument -> userDocument?.asExternal() }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFiance(): Flow<User?> = getLoginUser().flatMapLatest { loginUser ->
        if (loginUser == null) return@flatMapLatest flow { emit(null) }
        val fianceId = loginUser.fianceId ?: return@flatMapLatest flow { emit(null) }
        userFirebaseDataSource.getUserDocumentFlow(fianceId)
            .map { userDocument -> userDocument?.asExternal() }
    }

    override suspend fun connectWithFiance(
        fianceInvitationCode: String
    ): Boolean = externalScope.async {
        val fianceDocument = userFirebaseDataSource
            .getUserDocumentByInvitationCode(fianceInvitationCode)
            ?: return@async false

        val loginUserId = userFirebaseDataSource.loginUserId ?: return@async false

        val loginUserLinkAsync = userFirebaseDataSource
            .updateFianceId(loginUserId, fianceDocument.id)
            .asDeferred()

        val fianceLinkAsync = userFirebaseDataSource
            .updateFianceId(fianceDocument.id, loginUserId)
            .asDeferred()

        awaitAll(loginUserLinkAsync, fianceLinkAsync)
        return@async true
    }.await()

    override suspend fun changeLoginUserName(name: String) = externalScope.launch {
        val loginUserId = userFirebaseDataSource.loginUserId ?: return@launch
        userFirebaseDataSource.updateName(loginUserId, name)
    }.join()

    override suspend fun changeLoginUserMarriageDate(
        marriageDate: LocalDate
    ) = externalScope.launch(Dispatchers.IO) {
        val loginUserId = userFirebaseDataSource.loginUserId ?: return@launch
        userFirebaseDataSource.updateMarriageDate(loginUserId, marriageDate)
    }.join()

    override suspend fun logout() = externalScope.launch {
        preferencesDataSource.removeLoginUserId()
        userFirebaseDataSource.signOut()
    }.join()

    override suspend fun leave() = externalScope.launch(Dispatchers.IO) {
        val loginUserId = userFirebaseDataSource.loginUserId ?: return@launch
        val loginUser = userFirebaseDataSource.getUserDocument(loginUserId) ?: return@launch
        if (loginUser.fianceId != null) {
            userFirebaseDataSource.updateFianceId(loginUser.fianceId, null)
        }
        userFirebaseDataSource.delete(loginUserId)
        preferencesDataSource.removeLoginUserId()
    }.join()

    companion object {

        private const val INVITATION_CODE_LENGTH = 10
    }
}
