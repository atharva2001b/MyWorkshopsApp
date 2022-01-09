package com.example.myworkshops.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWorkshop( workshop: Workshop)

    @Query("SELECT * FROM workshop_table ORDER BY id")
    fun getAllWorkshops(): List<Workshop>

    @Query("SELECT * FROM workshop_table WHERE applied =:applied ORDER BY id")
    fun getSpecificWorkshops(applied: Boolean): List<Workshop>

}