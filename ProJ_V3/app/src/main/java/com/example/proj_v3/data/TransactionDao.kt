package com.example.proj_v3.data

import androidx.room.*
@Dao
interface TransactionDao {
    @Insert
    fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER BY id DESC")
    fun getAllTransactions(): List<Transaction>

    @Query("SELECT category, SUM(amount) as total FROM transactions GROUP BY category")
    fun getCategoryTotals(): List<CategoryTotal>

    @Query("SELECT SUM(amount) FROM `transactions`")
    suspend fun getTotalIncome(): Float?

    @Delete
    fun deleteTransaction(transaction: Transaction)
}