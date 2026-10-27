package com.route.domain.repo

import com.route.domain.data.AuthResult
import com.route.domain.data.Resource
import com.route.domain.data.SignInRequest
import com.route.domain.data.SignUpRequest
import com.route.domain.data.User
import kotlinx.coroutines.flow.Flow

interface AuthRepo {
    fun signUp(request: SignUpRequest): Flow<Resource<AuthResult?>>
    fun signIn(request: SignInRequest): Flow<Resource<AuthResult?>>
    fun signOut(): Flow<Resource<Unit>>
    fun getLoggedUser(): Flow<Resource<User?>>
}
