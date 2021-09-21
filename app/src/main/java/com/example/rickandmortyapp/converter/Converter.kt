package com.example.rickandmortyapp.converter

import androidx.room.TypeConverter
import com.example.rickandmortyapp.db.Location
import com.example.rickandmortyapp.db.Origin
import com.google.gson.Gson

class Converter {

    @TypeConverter
    fun convertFromLocation(location: Location): String {
        return location.name
    }

    @TypeConverter
    fun convertToLocation(name: String): Location {
        return Location(name, name)
    }

    @TypeConverter
    fun convertFromOrigin(origin: Origin): String {
        return origin.name
    }

    @TypeConverter
    fun convertToOrigin(name: String): Origin {
        return Origin(name, name)
    }

    @TypeConverter
    fun convertFromEpisode(episode: String): List<String> {
        return episode.split(",").map { it }
    }

    @TypeConverter
    fun convertToEpisode(episode: List<String>): String {
        return episode.joinToString(separator = ",")
    }
}