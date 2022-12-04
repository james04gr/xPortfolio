package com.xecoding.portfolio.remote

import com.xecoding.portfolio.Utils.ACCOUNT_DETAILS
import com.xecoding.portfolio.Utils.ACCOUNT_LIST
import com.xecoding.portfolio.Utils.TRANSACTIONS
import com.xecoding.portfolio.common.AbstractBaseKoinTest
import com.xecoding.portfolio.data.remote.api.PortfolioApi
import com.xecoding.portfolio.data.remote.dto.FilteredTransactionsRequest
import com.xecoding.portfolio.data.remote.dto.TransactionsRequest
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Test
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.test.assertEquals

class PortfolioApiKoinTest : AbstractBaseKoinTest() {

    private val mockWebServer: MockWebServer by inject()
    private val portfolioApi: PortfolioApi by inject()
    private val accounts: MockResponse by inject(named(ACCOUNT_LIST))
    private val accountDetails: MockResponse by inject(named(ACCOUNT_DETAILS))
    private val transactions: MockResponse by inject(named(TRANSACTIONS))

    override fun setUp() {
        super.setUp()
        mockWebServer.start()
    }

    override fun tearDown() {
        super.tearDown()
        mockWebServer.shutdown()
    }

    fun enqueue(response: MockResponse) {
        mockWebServer.enqueue(response)
    }

    fun takeRequest(): RecordedRequest {
        return mockWebServer.takeRequest()
    }

    @Test
    fun getAccounts() {
        runBlocking {
            enqueue(accounts)
            val accounts = portfolioApi.getAccounts()
            assertEquals(accounts[0].id, "1f34c76a-b3d1-43bc-af91-a82716f1bc2e")
            assertEquals(accounts[1].id, "d991edab-989f-44f9-bff9-ed4ed46a0e86")
            assertEquals(accounts[2].id, "af36e25f-a02e-4be2-b8c5-1667d4a0da8c")
            assertEquals(accounts[3].id, "c1132e36-19e2-472b-8d33-5dd07d46d2a4")
            assertEquals(accounts[4].id, "15778eec-c881-4593-bc4b-aabba7a0199f")

            assertEquals(accounts[0].account_number, 12345)
            assertEquals(accounts[0].balance, "99.00")
            assertEquals(accounts[0].currency_code, "EUR")
            assertEquals(accounts[0].account_type, "current")
            assertEquals(accounts[0].account_nickname, "My Salary")
        }
    }

    @Test
    fun getAccountDetails() {
        runBlocking {
            val accountId = "1f34c76a-b3d1-43bc-af91-a82716f1bc2e"
            enqueue(accountDetails)
            val accountDetails = portfolioApi.getAccountDetails(accountId)

            assertEquals(accountDetails.product_name, "Current Account EUR")
            assertEquals(accountDetails.opened_date, "2015-12-03T10:15:30Z")
            assertEquals(accountDetails.branch, "Main Branch")
            assertEquals(accountDetails.beneficiaries, listOf("John Doe", "Xenofon Karydis"))
        }
    }

    @Test
    fun getTransactions() {
        runBlocking {
            val accountId = "1f34c76a-b3d1-43bc-af91-a82716f1bc2e"
            enqueue(transactions)
            val transactionsDto = portfolioApi.getTransactions(accountId, TransactionsRequest(0))
            assertEquals(transactionsDto.transactions[0].id, "a1a9e85b-0f21-451b-813f-44ebabff46c9")
            assertEquals(transactionsDto.transactions[1].id, "a1a9e85b-0f21-451b-813f-44ebabff46c9")
            assertEquals(transactionsDto.transactions[2].id, "a1a9e85b-0f21-451b-813f-44ebabff46c9")
            assertEquals(transactionsDto.transactions[3].id, "a1a9e85b-0f21-451b-813f-44ebabff46c9")
            assertEquals(transactionsDto.transactions[4].id, "a1a9e85b-0f21-451b-813f-44ebabff46c9")
            assertEquals(transactionsDto.transactions[5].id, "a1a9e85b-0f21-451b-813f-44ebabff46c9")
            assertEquals(transactionsDto.transactions[6].id, "a1a9e85b-0f21-451b-813f-44ebabff46c9")

            assertEquals(transactionsDto.transactions[0].date, "2018-05-16T10:15:30Z")
            assertEquals(transactionsDto.transactions[0].transaction_amount, "199.21")
            assertEquals(transactionsDto.transactions[0].transaction_type, "intrabank")
            assertEquals(transactionsDto.transactions[0].description, null)
            assertEquals(transactionsDto.transactions[0].is_debit, false)

            assertEquals(transactionsDto.paging.pages_count, 1)
            assertEquals(transactionsDto.paging.total_items, 7)
            assertEquals(transactionsDto.paging.current_page, 0)
        }
    }

    @Test
    fun getFilteredTransactions() {
        runBlocking {
            val accountId = "1f34c76a-b3d1-43bc-af91-a82716f1bc2e"
            enqueue(transactions)
            val transactionsDto = portfolioApi.getFilteredTransactions(accountId, FilteredTransactionsRequest("2018-05-15T10:15:30Z", 0, "2018-05-16T10:15:30Z"))
            assertEquals(transactionsDto.transactions[0].id, "a1a9e85b-0f21-451b-813f-44ebabff46c9")
            assertEquals(transactionsDto.transactions[1].id, "a1a9e85b-0f21-451b-813f-44ebabff46c9")
            assertEquals(transactionsDto.transactions[2].id, "a1a9e85b-0f21-451b-813f-44ebabff46c9")
            assertEquals(transactionsDto.transactions[3].id, "a1a9e85b-0f21-451b-813f-44ebabff46c9")
            assertEquals(transactionsDto.transactions[4].id, "a1a9e85b-0f21-451b-813f-44ebabff46c9")
            assertEquals(transactionsDto.transactions[5].id, "a1a9e85b-0f21-451b-813f-44ebabff46c9")
            assertEquals(transactionsDto.transactions[6].id, "a1a9e85b-0f21-451b-813f-44ebabff46c9")

            assertEquals(transactionsDto.transactions[0].date, "2018-05-16T10:15:30Z")
            assertEquals(transactionsDto.transactions[0].transaction_amount, "199.21")
            assertEquals(transactionsDto.transactions[0].transaction_type, "intrabank")
            assertEquals(transactionsDto.transactions[0].description, null)
            assertEquals(transactionsDto.transactions[0].is_debit, false)

            assertEquals(transactionsDto.paging.pages_count, 1)
            assertEquals(transactionsDto.paging.total_items, 7)
            assertEquals(transactionsDto.paging.current_page, 0)
        }
    }
}