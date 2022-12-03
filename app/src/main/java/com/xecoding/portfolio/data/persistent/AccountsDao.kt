package com.xecoding.portfolio.data.persistent

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<Account>)

    @Query("""Select * from account order by is_favorite desc""")
    fun getAccounts(): Flow<List<Account>>

    @Query("""Select * from account where id == :accountId""")
    fun getAccount(accountId: String): Flow<Account>

    @Query("""Update account set is_favorite = 1 where id = :accountId""")
    fun setFavorite(accountId: String)

    @Query("""Update account set is_favorite = 0""")
    fun resetFavorite()

    @Query("""Select * from account""")
    suspend fun getDatabaseAccounts(): List<Account>

    @Query("""Delete from account""")
    fun deleteAccounts()

}