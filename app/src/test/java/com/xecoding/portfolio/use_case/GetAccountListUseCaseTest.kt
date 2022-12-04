package com.xecoding.portfolio.use_case

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.xecoding.portfolio.Utils
import com.xecoding.portfolio.Utils.toAccount
import com.xecoding.portfolio.common.NetworkResponse
import com.xecoding.portfolio.common.refreshAccounts
import com.xecoding.portfolio.domain.repository.accounts.AccountRepository
import com.xecoding.portfolio.domain.use_case.GetAccountListUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAccountListUseCaseTest {

    lateinit var getAccountListUseCase: GetAccountListUseCase

    @Mock
    lateinit var accountRepository: AccountRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        getAccountListUseCase = GetAccountListUseCase(accountRepository)
    }

    @Test
    fun getAccountsWithException() {
        runBlocking {
            Mockito.`when`(accountRepository.fetchAccounts()).thenAnswer {
                Utils.getAccountsList()
            }
            Mockito.`when`(accountRepository.databaseAccounts()).thenAnswer {
                Utils.getAccountsList().map { it.toAccount() }
            }
            val accounts = accountRepository.fetchAccounts()
            val databaseAccounts = accountRepository.databaseAccounts()
            val newAccounts = refreshAccounts(accounts, databaseAccounts)

            getAccountListUseCase().test {
                val loadingEmission = awaitItem()
                assertThat(loadingEmission is NetworkResponse.Loading).isTrue()

                val resultEmission = awaitItem()
                assertThat(resultEmission is NetworkResponse.Success || resultEmission is NetworkResponse.Error).isTrue()

                if (resultEmission is NetworkResponse.Success)
                    assertThat(newAccounts).isEqualTo(resultEmission.data)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}
