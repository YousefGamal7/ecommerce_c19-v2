package com.route.data.repos

import com.route.data.dataSource.AuthLocalDataSource
import com.route.data.dataSource.AuthOnlineDataSource
import com.route.domain.data.AuthResult
import com.route.domain.data.Resource
import com.route.domain.data.SignInRequest
import com.route.domain.data.SignUpRequest
import com.route.domain.data.User
import com.route.domain.repo.AuthRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val authOnlineDataSource: AuthOnlineDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
) : AuthRepo {

    override fun signUp(request: SignUpRequest): Flow<Resource<AuthResult?>> {
        return authOnlineDataSource.signUp(request)
            .onEach { resource ->
                if (resource is Resource.Success && resource.data != null) {
                    authLocalDataSource.saveSession(
                        user = resource.data.user,
                        token = resource.data.token,
                    )
                }
            }
    }

    override fun signIn(request: SignInRequest): Flow<Resource<AuthResult?>> {
        return authOnlineDataSource.signIn(request)
            .onEach { resource ->
                if (resource is Resource.Success && resource.data != null) {
                    authLocalDataSource.saveSession(
                        user = resource.data.user,
                        token = resource.data.token,
                    )
                }
            }
    }

    override fun signOut(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            authLocalDataSource.clearSession()
            emit(Resource.Success(Unit))
        } catch (t: Throwable) {
            emit(Resource.Error(t))
        }
    }

    override fun getLoggedUser(): Flow<Resource<User?>> = flow {
        emit(Resource.Loading())
        try {
            val user = authLocalDataSource.getLoggedUser()
            emit(Resource.Success(user))
        } catch (t: Throwable) {
            emit(Resource.Error(t))
        }
    }
}
