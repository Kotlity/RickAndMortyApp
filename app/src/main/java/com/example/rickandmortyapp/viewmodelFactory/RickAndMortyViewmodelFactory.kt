package com.example.rickandmortyapp.viewmodelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmortyapp.repository.RickAndMortyRepository
import com.example.rickandmortyapp.viewmodel.RickAndMortyViewModel

class RickAndMortyViewmodelFactory(val rickAndMortyRepository: RickAndMortyRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RickAndMortyViewModel(rickAndMortyRepository) as T
    }
}