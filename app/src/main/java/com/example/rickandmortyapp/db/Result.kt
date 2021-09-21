package com.example.rickandmortyapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rickandmortyapp.db.Location
import com.example.rickandmortyapp.db.Origin
import java.io.Serializable

@Entity
data class Result(
    val created: String,
    val episode: List<String>,
    val gender: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val image: String,
    val location: Location,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: String,
    val type: String,
    val url: String
): Serializable