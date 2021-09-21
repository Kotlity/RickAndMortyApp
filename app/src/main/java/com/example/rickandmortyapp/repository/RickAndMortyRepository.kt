package com.example.rickandmortyapp.repository

import com.example.rickandmortyapp.db.ResultDatabase
import com.example.rickandmortyapp.db.Result
import com.example.rickandmortyapp.retrofitservice.RetrofitService

class RickAndMortyRepository(val retrofitService: RetrofitService, val resultDatabase: ResultDatabase) {

    suspend fun getAllCharacters(pageNumber: Int) = retrofitService.getAllCharacters(pageNumber)

    suspend fun getSearchCharacter(nameSearch: String) = retrofitService.getSearchCharacter(nameSearch)

    suspend fun addCharacter(character: Result) = resultDatabase.resultDao().addCharacter(character)

    suspend fun deleteCharacter(character: Result) = resultDatabase.resultDao().deleteCharacter(character)

    suspend fun deleteAllCharacters() = resultDatabase.resultDao().deleteAllCharacters()

    fun getAllCharactersRoom() = resultDatabase.resultDao().getAllCharactersRoom()
}