package com.capstone.capstonetim.ui.option

import androidx.lifecycle.ViewModel
import com.capstone.capstonetim.database.model.PlaceResponse
import com.capstone.capstonetim.database.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class OptionViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {

    suspend fun uploadUserPreference(preferences: String): Flow<Result<List<PlaceResponse>>> = apiRepository.uploadUserPreference(preferences)

    fun getRandomPlace(): Flow<Result<PlaceResponse>> =
        apiRepository.getRandomPlace()

}