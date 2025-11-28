package com.example.huerto.data.api

data class GeocodingResponse(
    val results: List<GeoResult>,
    val status: String
)

data class GeoResult(
    val geometry: Geometry
)

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)
