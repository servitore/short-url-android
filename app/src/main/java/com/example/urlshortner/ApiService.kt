package com.example.urlshortner

import com.example.urlshortner.models.LinkResponse
import com.example.urlshortner.models.dto.LinkDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Authorization: Bearer token", "Content-Type: application/json")
    @POST("v4/shorten")
    suspend fun createLink(@Body link: LinkDto): LinkResponse?
}