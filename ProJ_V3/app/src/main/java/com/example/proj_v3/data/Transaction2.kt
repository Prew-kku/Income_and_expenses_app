package com.example.proj_v3.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions2")
data class Transaction2(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val amount: Float
)