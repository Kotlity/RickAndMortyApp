package com.example.rickandmortyapp.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCharacter(character: Result)

    @Delete
    suspend fun deleteCharacter(character: Result)

    @Query("DELETE FROM result")
    suspend fun deleteAllCharacters()

    @Query("SELECT * FROM result ")
    fun getAllCharactersRoom(): LiveData<List<Result>>
}