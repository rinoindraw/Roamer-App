package com.capstone.capstonetim.database.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)