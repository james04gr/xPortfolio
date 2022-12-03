package com.xecoding.portfolio.data.persistent

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class Account(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "nickname") val account_nickname: String?,
    @ColumnInfo(name = "number") val account_number: Int,
    @ColumnInfo(name = "type") val account_type: String,
    @ColumnInfo(name = "balance") val balance: String,
    @ColumnInfo(name = "currency") val currency_code: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Int

)
