package com.example.rickandmortyapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.db.Characters
import com.example.rickandmortyapp.db.Result
import com.example.rickandmortyapp.repository.RickAndMortyRepository
import com.example.rickandmortyapp.response.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class RickAndMortyViewModel(val rickAndMortyRepository: RickAndMortyRepository): ViewModel() {

    val listOfCharacters: MutableLiveData<Resource<Characters>> = MutableLiveData()
    var listOfCharactersPageNumber = 1
    var listOfCharactersResponse: Characters? = null

    val listOfSearchCharacters: MutableLiveData<Resource<Characters>> = MutableLiveData()


    fun getAllCharacters() = viewModelScope.launch {
        listOfCharacters.postValue(Resource.Loading())
        val response = rickAndMortyRepository.getAllCharacters(listOfCharactersPageNumber)
        listOfCharacters.postValue(handleListOfCharacters(response))
    }

    fun getSearchCharacter(nameSearch: String) = viewModelScope.launch {
        listOfSearchCharacters.postValue(Resource.Loading())
        val response = rickAndMortyRepository.getSearchCharacter(nameSearch)
        listOfSearchCharacters.postValue(handleListOfSearchCharacters(response))
    }


    private fun handleListOfCharacters(response: Response<Characters>): Resource<Characters> {
        if (response.isSuccessful) {
            response.body()?.let { retrofitResponse ->  // that is the response we got from RetrofitAPI
                listOfCharactersPageNumber++
                if (listOfCharactersResponse == null) {   // this block of code will be executed
                    listOfCharactersResponse = retrofitResponse  //  if the first page is loaded
                } else {
                    val oldResult = listOfCharactersResponse?.results
                    val newResult = retrofitResponse.results
                    oldResult?.addAll(newResult)
                }
                return Resource.Success(listOfCharactersResponse ?: retrofitResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleListOfSearchCharacters(response: Response<Characters>) : Resource<Characters> {
        if(response.isSuccessful) {
            response.body()?.let { retrofitResponse ->
                return Resource.Success(retrofitResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun addCharacter(character: Result) = viewModelScope.launch {
        rickAndMortyRepository.addCharacter(character)
    }

    fun deleteCharacter(character: Result) = viewModelScope.launch {
        rickAndMortyRepository.deleteCharacter(character)
    }

    fun deleteAllCharacters() = viewModelScope.launch {
        rickAndMortyRepository.deleteAllCharacters()
    }

    fun getAllCharactersRoom() = rickAndMortyRepository.getAllCharactersRoom()

}