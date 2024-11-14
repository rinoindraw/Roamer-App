package com.capstone.capstonetim.ui.login

import androidx.lifecycle.ViewModel
import com.capstone.capstonetim.database.model.LoginResponse
import com.capstone.capstonetim.database.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    suspend fun loginUser(email: String, password: String): Flow<Result<LoginResponse>> = authRepository.loginUser(email, password)

}