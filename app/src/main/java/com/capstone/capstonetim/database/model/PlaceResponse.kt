package com.capstone.capstonetim.database.model

data class PlaceResponse(
    val id: Int,
    val name: String,
    val city: String,
    val category: String,
    val description: String,
    val img_link: String
)