package com.example.rickandmortyapp.retrofitservice

import com.example.rickandmortyapp.db.Characters
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET("character")
    suspend fun getAllCharacters(@Query("page") pageNumber: Int = 1): Response<Characters>

    @GET("character")
    suspend fun getSearchCharacter(@Query("name") nameSearch: String): Response<Characters>

    companion object {
        var retrofitInstance: RetrofitService? = null
        fun getResponse(): RetrofitService {
            if (retrofitInstance == null) {
                val retrofitBuilder = Retrofit.Builder()
                    .baseUrl("https://rickandmortyapi.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitInstance = retrofitBuilder.create(RetrofitService::class.java)
            }
            return retrofitInstance!!
        }
    }
}