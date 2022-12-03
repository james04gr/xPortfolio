package com.xecoding.portfolio.data.persistent

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(details: Details)

    @Query("""Select * from details where id = :accountId""")
    suspend fun getDetails(accountId: String): Details?

    @Query("""Delete from details""")
    fun delete()

    @Transaction
    fun refreshDetails(details: Details) {
        delete()
        insert(details)
    }

}