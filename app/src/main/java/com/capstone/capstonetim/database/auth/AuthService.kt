package com.capstone.capstonetim.database.auth

import com.capstone.capstonetim.database.model.LoginRequest
import com.capstone.capstonetim.database.model.LoginResponse
import com.capstone.capstonetim.database.model.RegisterRequest
import com.capstone.capstonetim.database.model.RegisterResponse
import retrofit2.http.*

interface AuthService {

    @Headers("Content-Type: application/json")
    @POST("/user/login")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): LoginResponse

    @Headers("Content-Type: application/json")
    @POST("/user/signup")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): RegisterResponse

}