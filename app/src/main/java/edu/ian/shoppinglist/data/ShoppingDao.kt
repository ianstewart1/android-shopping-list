package edu.ian.shoppinglist.data

import androidx.room.*

@Dao
interface ShoppingDao {

    @Query("SELECT * FROM shopping")
    fun getAllShopping() : List<Shopping>

    @Insert
    fun addShopping(shopping: Shopping) : Long

    @Delete
    fun deleteShopping(shopping: Shopping)

    @Update
    fun updateShopping(shopping: Shopping)

    @Query("DELETE FROM shopping")
    fun deleteAllShopping()
}