package com.capstone.capstonetim.database.api

import com.capstone.capstonetim.database.model.PlaceResponse
import com.capstone.capstonetim.database.model.PreferenceRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json")
    @GET("/user/home")
    suspend fun getAllPlaces(): List<PlaceResponse>

    @Headers("Content-Type: application/json")
    @GET("/places/")
    suspend fun getPlacesDefault(): List<PlaceResponse>

    @Headers("Content-Type: application/json")
    @POST("/user/preference")
    suspend fun uploadUserPreference(@Body preferences: PreferenceRequest): List<PlaceResponse>


}


