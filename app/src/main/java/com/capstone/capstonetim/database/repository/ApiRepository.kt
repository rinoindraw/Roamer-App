package com.capstone.capstonetim.database.repository

import com.capstone.capstonetim.database.api.ApiService
import com.capstone.capstonetim.database.model.PlaceResponse
import com.capstone.capstonetim.database.model.PreferenceRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiService: ApiService,

    ) {
    fun getAllPlaces(): Flow<Result<List<PlaceResponse>>> = flow {
        try {
            val placesResponse = apiService.getAllPlaces()
            emit(Result.success(placesResponse))
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(Result.failure(exception))
        }
    }

    fun getRandomPlace(): Flow<Result<PlaceResponse>> = flow {
        try {
            val placesResponse = apiService.getPlacesDefault()
            if (placesResponse.isNotEmpty()) {
                val randomPlace = placesResponse.random()
                emit(Result.success(randomPlace))
            } else {
                emit(Result.failure(Exception("No places available")))
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(Result.failure(exception))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun uploadUserPreference(preferences: String): Flow<Result<List<PlaceResponse>>> = flow {
        try {
            val request = PreferenceRequest(preferences)
            val response = apiService.uploadUserPreference(request)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}