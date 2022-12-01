package com.xecoding.portfolio.domain.use_case

import com.xecoding.portfolio.common.NetworkResponse
import com.xecoding.portfolio.data.remote.dto.AccountDto
import com.xecoding.portfolio.domain.repository.AccountRepository
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONArray

class GetAccountListUseCase(
    private val accountRepository: AccountRepository
) {

    private val response = "[{\"id\":\"1f34c76a-b3d1-43bc-af91-a82716f1bc2e\",\"account_number\":12345,\"balance\":\"99.00\",\"currency_code\":\"EUR\",\"account_type\":\"current\",\"account_nickname\":\"My Salary\"},{\"id\":\"d991edab-989f-44f9-bff9-ed4ed46a0e86\",\"account_number\":54321,\"balance\":\"99200450316.00\",\"currency_code\":\"GBP\",\"account_type\":\"current\",\"account_nickname\":null},{\"id\":\"af36e25f-a02e-4be2-b8c5-1667d4a0da8c\",\"account_number\":23456,\"balance\":\"100001.00\",\"currency_code\":\"EUR\",\"account_type\":\"savings\",\"account_nickname\":\"My Savings\"},{\"id\":\"c1132e36-19e2-472b-8d33-5dd07d46d2a4\",\"account_number\":65432,\"balance\":\"220002.00\",\"currency_code\":\"EUR\",\"account_type\":\"time\",\"account_nickname\":\"Retirement\"},{\"id\":\"15778eec-c881-4593-bc4b-aabba7a0199f\",\"account_number\":34567,\"balance\":\"0.00\",\"currency_code\":\"EUR\",\"account_type\":\"credit card\",\"account_nickname\":null}]"

    operator fun invoke(): Flow<NetworkResponse<List<AccountDto>>> = flow {
        try {
            emit(NetworkResponse.Loading())
            delay(2000)
            // val allAccounts = accountRepository.getAccounts()
            // emit(NetworkResponse.Success(allAccounts))
            emit(NetworkResponse.Success(getAccountsDummy()))
            //throw java.lang.Exception("Hello re malakaes")
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkResponse.Error(message = e.message ?: "An unexpected error occurred"))
        }
    }

    private fun getAccountsDummy(): List<AccountDto> {
        val jsonArray = JSONArray(response)
        val result = mutableListOf<AccountDto>()

        for (i in 0 until jsonArray.length()) {
            val account = jsonArray.getJSONObject(i)
            result.add(Gson().fromJson(account.toString(), AccountDto::class.java))
        }

        return result
    }

}