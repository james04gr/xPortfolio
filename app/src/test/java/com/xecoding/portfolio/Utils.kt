package com.xecoding.portfolio

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xecoding.portfolio.data.persistent.Account
import com.xecoding.portfolio.data.persistent.Details
import com.xecoding.portfolio.data.remote.dto.AccountDetailsDto
import com.xecoding.portfolio.data.remote.dto.AccountDto
import java.lang.reflect.Type

object Utils {

    const val ACCOUNT_LIST = "account_list"
    const val ACCOUNT_DETAILS = "account_details"
    const val DETAILS = "details"
    const val TRANSACTIONS = "transactions"

    fun readTestResourceFile(fileName: String): String {
        return javaClass.classLoader?.getResourceAsStream(fileName)?.bufferedReader()
            .use { bufferReader -> bufferReader?.readText() } ?: ""
    }

    fun getAccountsList(): List<AccountDto> {
        val string = readTestResourceFile("${ACCOUNT_LIST.lowercase()}.json")
        val listOfMyClassObject: Type = object : TypeToken<ArrayList<AccountDto?>?>() {}.type
        return Gson().fromJson(string, listOfMyClassObject)
    }

    fun getAccountDetails(): AccountDetailsDto {
        val string = readTestResourceFile("${ACCOUNT_DETAILS.lowercase()}.json")
        val listOfMyClassObject: Type = object : TypeToken<AccountDetailsDto>() {}.type
        return Gson().fromJson(string, listOfMyClassObject)
    }

    fun getDetails(): Details {
        val string = readTestResourceFile("${DETAILS.lowercase()}.json")
        val listOfMyClassObject: Type = object : TypeToken<Details>() {}.type
        return Gson().fromJson(string, listOfMyClassObject)
    }

    fun AccountDto.toAccount(isFavorite: Int = 0): Account =
        Account(
            id = id,
            account_nickname = account_nickname,
            account_number = account_number,
            account_type = account_type,
            balance = balance,
            currency_code = currency_code,
            isFavorite = isFavorite
        )

}