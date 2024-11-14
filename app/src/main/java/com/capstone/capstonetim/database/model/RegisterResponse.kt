package com.capstone.capstonetim.database.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("email")
    val email: String,
    @field:SerializedName("message")
    val message: String,

    )
