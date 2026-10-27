package com.route.domain.usecase

import com.route.domain.data.Resource
import com.route.domain.repo.AuthRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepo: AuthRepo,
) {
    fun signOut(): Flow<Resource<Unit>> {
        return authRepo.signOut()
    }
}
