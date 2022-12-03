package com.xecoding.portfolio.data.persistent

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "details")
data class Details(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "beneficiaries") val beneficiaries: List<String> = emptyList(),
    @ColumnInfo(name = "branch") val branch: String,
    @ColumnInfo(name = "date") val opened_date: String,
    @ColumnInfo(name = "product_name") val product_name: String
)