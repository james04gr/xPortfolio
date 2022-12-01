package com.example.portfolio.domain.use_case

import com.example.portfolio.common.NetworkResponse
import com.example.portfolio.data.remote.dto.AccountDetailsDto
import com.example.portfolio.domain.repository.AccountRepository
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAccountDetailsUseCase(
    private val accountRepository: AccountRepository
) {

    private val responseDummy = "{\"product_name\":\"Current Account EUR\",\"opened_date\":\"2015-12-03T10:15:30Z\",\"branch\":\"Main Branch\",\"beneficiaries\":[\"John Doe, Xenofon Karydis, Terson Yokalele, Makelele Mbape, Afta k alla polla\"]}"

    operator fun invoke(accountId: String): Flow<NetworkResponse<AccountDetailsDto>> = flow {
        try {
            emit(NetworkResponse.Loading())
            delay(2000)
//            val accountDetails = accountRepository.getAccountDetails(accountId)
//            emit(NetworkResponse.Success(accountDetails))
            emit(NetworkResponse.Success(Gson().fromJson(responseDummy, AccountDetailsDto::class.java)))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkResponse.Error(message = e.message ?: "An unexpected error occurred"))
        }
    }

}