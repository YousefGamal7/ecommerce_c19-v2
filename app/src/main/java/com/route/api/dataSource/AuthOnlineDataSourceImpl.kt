package com.route.api.dataSource

import com.route.api.ApiServices
import com.route.api.data.SignInRequestDto
import com.route.api.data.SignUpRequestDto
import com.route.api.helpers.safeAuthCall
import com.route.data.dataSource.AuthOnlineDataSource
import com.route.domain.data.AuthResult
import com.route.domain.data.Resource
import com.route.domain.data.SignInRequest
import com.route.domain.data.SignUpRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthOnlineDataSourceImpl @Inject constructor(
    private val apiServices: ApiServices,
) : AuthOnlineDataSource {

    override fun signUp(request: SignUpRequest): Flow<Resource<AuthResult?>> {
        return safeAuthCall(
            api = {
                apiServices.signUp(
                    SignUpRequestDto(
                        name = request.name,
                        email = request.email,
                        password = request.password,
                        rePassword = request.rePassword,
                        phone = request.phone,
                    )
                )
            },
            converter = { dto -> dto.toAuthResult() }
        )
    }

    override fun signIn(request: SignInRequest): Flow<Resource<AuthResult?>> {
        return safeAuthCall(
            api = {
                apiServices.signIn(
                    SignInRequestDto(
                        email = request.email,
                        password = request.password,
                    )
                )
            },
            converter = { dto -> dto.toAuthResult() }
        )
    }
}
