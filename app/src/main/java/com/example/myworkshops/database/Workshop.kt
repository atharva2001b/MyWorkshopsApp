package com.example.myworkshops.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "workshop_table")
data class Workshop (
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val price: String,
    var applied: Boolean,
    var image: Int
        )