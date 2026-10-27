package com.route.domain.usecase

import com.route.domain.data.Resource
import com.route.domain.data.User
import com.route.domain.repo.AuthRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLoggedUserUseCase @Inject constructor(
    private val authRepo: AuthRepo,
) {
    fun getLoggedUser(): Flow<Resource<User?>> {
        return authRepo.getLoggedUser()
    }
}
