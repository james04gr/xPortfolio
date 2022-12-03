package com.xecoding.portfolio.data.persistent

import androidx.room.TypeConverter

class BeneficiariesConverter {
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(",")
    }

    @TypeConverter
    fun toString(value: List<String>?): String? {
        return value?.joinToString(separator = ",")
    }
}