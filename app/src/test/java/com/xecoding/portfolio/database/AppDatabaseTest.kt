package com.xecoding.portfolio.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.xecoding.portfolio.Utils
import com.xecoding.portfolio.Utils.toAccount
import com.xecoding.portfolio.data.persistent.AccountsDao
import com.xecoding.portfolio.data.persistent.AppDatabase
import com.xecoding.portfolio.data.persistent.DetailsDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var accountsDao: AccountsDao
    private lateinit var detailsDao: DetailsDao
    private lateinit var db: AppDatabase

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).allowMainThreadQueries().build()
        accountsDao = db.accountsDao()
        detailsDao = db.detailsDao()
    }

    @After
    fun closeDb() {
        db.close()
        stopKoin()
    }

    @Test
    fun insertAccounts() {
        runBlocking {
            val accounts = Utils.getAccountsList().map { it.toAccount() }
            db.accountsDao().insert(accounts)
            val result = db.accountsDao().databaseAccounts()
            assertEquals(result, accounts)
        }
    }

    @Test
    fun setFavorite() {
        runBlocking {
            val accountId = "1f34c76a-b3d1-43bc-af91-a82716f1bc2e"
            Utils.getAccountsList().map { it.toAccount() }.find { it.id == accountId }?.let {
                db.accountsDao().insert(listOf(it))
                db.accountsDao().setFavorite(accountId)
                db.accountsDao().databaseAccounts().find { a -> a.id == accountId }?.let { r ->
                    assertEquals(r.isFavorite, 1)
                }
            }
        }
    }

    @Test
    fun resetFavorite() {
        runBlocking {
            val accounts = Utils.getAccountsList().map { it.toAccount(isFavorite = 1) }
            db.accountsDao().insert(accounts)
            db.accountsDao().resetFavorite()
            val result = db.accountsDao().databaseAccounts()
            result.forEach {
                assertEquals(it.isFavorite, 0)
            }
        }
    }

    @Test
    fun deleteAccounts() {
        runBlocking {
            val accounts = Utils.getAccountsList().map { it.toAccount() }
            db.accountsDao().insert(accounts)
            db.accountsDao().deleteAccounts()
            val result = db.accountsDao().databaseAccounts()
            Assert.assertEquals(result.size, 0)
        }
    }
}