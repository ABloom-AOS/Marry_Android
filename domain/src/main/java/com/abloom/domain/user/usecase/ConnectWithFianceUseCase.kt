package com.abloom.domain.user.usecase

import com.abloom.domain.user.repository.UserRepository
import javax.inject.Inject

class ConnectWithFianceUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    /**
     * @return 연결 결과를 반환합니다. 연결에 실패하면 false를 반환합니다.
     */
    suspend operator fun invoke(fianceInvitationCode: String): Boolean {
        val loginUserId = userRepository.loginUserId
        val fiance = userRepository.getUserByInvitationCode(fianceInvitationCode) ?: return false
        if (fiance.id == loginUserId) return false
        userRepository.connectWith(fiance)
        return true
    }
}
