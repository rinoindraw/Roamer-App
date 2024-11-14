package com.capstone.capstonetim.database.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("message")
    val message: String? = null,
    @field:SerializedName("id")
    val id: String? = null,
    @field:SerializedName("email")
    val email: String? = null,
    @field:SerializedName("name")
    val name: String? = null,

    )