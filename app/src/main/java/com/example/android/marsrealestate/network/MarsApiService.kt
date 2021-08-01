package com.example.android.marsrealestate.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface MarsApiService {


    //this is enqueue method
    @GET("realestate")
    fun getProperties(@Query("filter") type: String) : Deferred<List<MarsProperty>>
}


////this is enqueue method
//@GET("realestate")
//fun getProperties() : Call<List<MarsProperty>>