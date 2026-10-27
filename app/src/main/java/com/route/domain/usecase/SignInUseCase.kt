package com.route.domain.usecase

import com.route.domain.data.AuthResult
import com.route.domain.data.Resource
import com.route.domain.data.SignInRequest
import com.route.domain.repo.AuthRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepo: AuthRepo,
) {
    fun signIn(request: SignInRequest): Flow<Resource<AuthResult?>> {
        return authRepo.signIn(request)
    }
}
