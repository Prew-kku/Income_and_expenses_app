package com.example.proj_v3.data

import androidx.room.*

@Dao
interface Transaction2Dao {
    @Insert
    fun insertTransaction(transaction: Transaction2)


    @Query("SELECT * FROM transactions2 ORDER BY id DESC")
    fun getAllTransactions(): List<Transaction2>


    @Query("SELECT category, SUM(amount) as total FROM transactions2 GROUP BY category")
    fun getCategoryTotals(): List<CategoryTotal>

    @Query("SELECT SUM(amount) FROM `transactions2`")
    suspend fun getTotalExpense(): Float?

    @Delete
    fun deleteTransaction(transaction: Transaction2)
}