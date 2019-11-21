package edu.ian.shoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "shopping")
data class Shopping(
    @PrimaryKey(autoGenerate = true) var shoppingId: Long?,
    @ColumnInfo(name ="type") var type: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "price") var price: String,
    @ColumnInfo(name = "isBought") var isBought: Boolean
) : Serializable