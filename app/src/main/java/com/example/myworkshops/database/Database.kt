package com.example.myworkshops.database

import android.content.Context
import android.os.strictmode.InstanceCountViolation
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext


@Database(entities = [ Workshop::class], version = 1, exportSchema = false)
abstract class Database: RoomDatabase() {

    abstract fun dao(): Dao

    companion object{
        @Volatile
        private var INSTANCE: com.example.myworkshops.database.Database? = null

        fun getDatabase( context: Context, userid: String): com.example.myworkshops.database.Database{

            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, com.example.myworkshops.database.Database::class.java, "database-${userid}").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}