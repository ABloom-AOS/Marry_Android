package com.abloom.domain.user.usecase

import com.abloom.domain.user.model.User
import com.abloom.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFianceUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(): Flow<User?> = userRepository.getFianceFlow()
}
