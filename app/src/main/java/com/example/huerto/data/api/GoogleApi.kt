package com.example.huerto.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleApi {

    @GET("geocode/json")
    suspend fun geocode(
        @Query("address") address: String,
        @Query("key") key: String
    ): GeocodingResponse
}
