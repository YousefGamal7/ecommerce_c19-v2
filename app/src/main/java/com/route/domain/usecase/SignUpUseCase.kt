package com.route.domain.usecase

import com.route.domain.data.AuthResult
import com.route.domain.data.Resource
import com.route.domain.data.SignUpRequest
import com.route.domain.repo.AuthRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepo: AuthRepo,
) {
    fun signUp(request: SignUpRequest): Flow<Resource<AuthResult?>> {
        return authRepo.signUp(request)
    }
}
