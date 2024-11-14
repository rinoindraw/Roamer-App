package com.capstone.capstonetim.ui.register

import androidx.lifecycle.ViewModel
import com.capstone.capstonetim.database.model.RegisterResponse
import com.capstone.capstonetim.database.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    suspend fun registerUser(name: String, username: String, password: String): Flow<Result<RegisterResponse>> =
        authRepository.userRegister(name, username, password)

}