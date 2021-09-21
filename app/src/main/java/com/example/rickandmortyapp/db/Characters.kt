package com.example.rickandmortyapp.db

data class Characters(
    val info: Info,
    val results: MutableList<Result>
)