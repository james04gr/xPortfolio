package com.xecoding.portfolio.use_case

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.xecoding.portfolio.Utils
import com.xecoding.portfolio.common.NetworkResponse
import com.xecoding.portfolio.domain.repository.accounts.AccountRepository
import com.xecoding.portfolio.domain.use_case.GetAccountDetailsUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAccountDetailsUseCaseTest {

    private val accountId = "1f34c76a-b3d1-43bc-af91-a82716f1bc2e"
    lateinit var getAccountDetailsUseCase: GetAccountDetailsUseCase

    @Mock
    lateinit var accountRepository: AccountRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        getAccountDetailsUseCase = GetAccountDetailsUseCase(accountRepository)
    }

    @Test
    fun getAccountDetails() {
        runBlocking {
            Mockito.`when`(accountRepository.getAccountDetails(accountId)).thenAnswer {
                Utils.getAccountDetails()
            }
            Mockito.`when`(accountRepository.getDetails(accountId)).thenAnswer {
                Utils.getDetails()
            }

            val accountDetails = accountRepository.getAccountDetails(accountId)
            val details = accountRepository.getDetails(accountId)

            getAccountDetailsUseCase(accountId).test {
                val loadingEmission = awaitItem()
                assertThat(loadingEmission is NetworkResponse.Loading).isTrue()

                val resultEmission = awaitItem()
                assertThat(resultEmission is NetworkResponse.Success || resultEmission is NetworkResponse.Error).isTrue()

                if (resultEmission is NetworkResponse.Success) assertThat(accountDetails).isEqualTo(
                    resultEmission.data
                )
                else if (resultEmission is NetworkResponse.Error) assertThat(details).isEqualTo(
                    resultEmission.data
                )

                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}