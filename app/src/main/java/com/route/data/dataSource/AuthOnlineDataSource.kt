package com.route.data.dataSource

import com.route.domain.data.AuthResult
import com.route.domain.data.Resource
import com.route.domain.data.SignInRequest
import com.route.domain.data.SignUpRequest
import kotlinx.coroutines.flow.Flow

interface AuthOnlineDataSource {
    fun signUp(request: SignUpRequest): Flow<Resource<AuthResult?>>
    fun signIn(request: SignInRequest): Flow<Resource<AuthResult?>>
}
