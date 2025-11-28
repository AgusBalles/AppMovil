package com.example.huerto.data.api

class GoogleRepository {
    suspend fun getCoords(address: String, key: String): Pair<Double, Double>? {
        val res = GoogleApiService.api.geocode(address, key)

        return if (res.status == "OK" && res.results.isNotEmpty()) {
            val loc = res.results[0].geometry.location
            loc.lat to loc.lng
        } else null
    }
}
