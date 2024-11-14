package com.capstone.capstonetim.database.repository

import com.capstone.capstonetim.database.auth.AuthService
import com.capstone.capstonetim.database.model.LoginRequest
import com.capstone.capstonetim.database.model.LoginResponse
import com.capstone.capstonetim.database.model.RegisterRequest
import com.capstone.capstonetim.database.model.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService,
) {

    suspend fun loginUser(email: String, password: String): Flow<Result<LoginResponse>> = flow {
        try {
            val request = LoginRequest(email, password)
            val response = authService.loginUser(request)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun userRegister(
        name: String,
        username: String,
        password: String
    ): Flow<Result<RegisterResponse>> = flow {
        try {
            val request = RegisterRequest(name, username, password)
            val response = authService.registerUser(request)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

}