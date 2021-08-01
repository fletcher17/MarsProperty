/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.marsrealestate.network.MarsApiFilter
import com.example.android.marsrealestate.network.MarsProperty
import com.example.android.marsrealestate.network.MarsRetrofit
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    enum class MarsResponseState { LOADING, SUCCESS, FAILURE }

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<MarsResponseState>()
    // The external immutable LiveData for the request status String
    val status: LiveData<MarsResponseState>
        get() = _status

    private val _response = MutableLiveData<List<MarsProperty>>()

    val response: LiveData<List<MarsProperty>> get() = _response

    private val _navigateDetails = MutableLiveData<MarsProperty>()
    val navigateDetails: LiveData<MarsProperty> get() = _navigateDetails

  //  val viewModelJob = Job()
//    val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     * Enqueue was used using the callback method
     */
    private fun getMarsRealEstateProperties(filter: MarsApiFilter) {
        viewModelScope.launch(IO) {
            var getMarspropertyDeferred = MarsRetrofit.apiService.getProperties(filter.value)
            withContext(Main) {
                try {
                    _status.value = MarsResponseState.LOADING
                    val loadedData = getMarspropertyDeferred.await()
                    _status.value = MarsResponseState.SUCCESS
                    if (loadedData.isNotEmpty()) {
                        _response.value = loadedData
                    }
                } catch (e: Throwable) {
                    _status.value = MarsResponseState.FAILURE
                    _response.value = ArrayList()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun displayMarsDetails(marsDetails: MarsProperty) {
        _navigateDetails.value = marsDetails
    }

    fun displayMarsDetailsComplete() {
        _navigateDetails.value = null
    }

    fun updateFilter(filter: MarsApiFilter) {
        getMarsRealEstateProperties(filter)
    }
}

////This is how to use enqueue to retrieve data
//private fun getMarsRealEstateProperties() {
//    MarsRetrofit.apiService.getProperties().enqueue(object : Callback<List<MarsProperty>> {
//        override fun onResponse(call: Call<List<MarsProperty>>, response: Response<List<MarsProperty>>) {
//            _response.value = "Succes: ${response.body()?.size} Mars property retrieved"
//        }
//
//        override fun onFailure(call: Call<List<MarsProperty>>, t: Throwable) {
//            _response.value = "E no dey: " + t.message
//        }
//    })
//}
