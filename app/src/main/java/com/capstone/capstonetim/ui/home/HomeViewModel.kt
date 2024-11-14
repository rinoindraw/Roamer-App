package com.capstone.capstonetim.ui.home

import androidx.lifecycle.ViewModel
import com.capstone.capstonetim.database.model.PlaceResponse
import com.capstone.capstonetim.database.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {

    fun getPlaces(): Flow<Result<List<PlaceResponse>>> =
        apiRepository.getAllPlaces()

}