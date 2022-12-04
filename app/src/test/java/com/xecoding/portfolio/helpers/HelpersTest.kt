package com.xecoding.portfolio.helpers

import com.google.common.truth.Truth.assertThat
import com.xecoding.portfolio.common.displayName
import com.xecoding.portfolio.common.refreshAccounts
import com.xecoding.portfolio.data.persistent.Account
import com.xecoding.portfolio.data.remote.dto.AccountDto
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HelpersTest {

    @Test
    fun displayNameTest() {
        val account = Account(
            id = "",
            account_nickname = null,
            account_number = 54321,
            account_type = "",
            balance = "",
            currency_code = "",
            isFavorite = 0
        )

        val account2 = Account(
            id = "",
            account_nickname = "Account nickname",
            account_number = 54321,
            account_type = "",
            balance = "",
            currency_code = "",
            isFavorite = 0
        )

        assertThat(account.displayName() != account.account_nickname).isTrue()
        assertThat(account.displayName() == account.account_number.toString()).isTrue()

        assertThat(account2.displayName() == account2.account_nickname).isTrue()
        assertThat(account2.displayName() != account2.account_number.toString()).isTrue()
    }

    @Test
    fun refreshAccountTest() {
        val accountDtoList = listOf(
            AccountDto(id = "aaa", account_nickname = null, account_number = 10, account_type = "t", balance = "0", currency_code = ""),
            AccountDto(id = "bbb", account_nickname = "bbb", account_number = 20, account_type = "t", balance = "0", currency_code = ""),
            AccountDto(id = "ccc", account_nickname = "ccc", account_number = 30, account_type = "t", balance = "0", currency_code = "")
        )
        val accountList = listOf(
            Account(id = "aaa", account_nickname = "aaa", account_number = 0, account_type = "", balance = "10", currency_code = "", isFavorite = 0),
            Account(id = "bbb", account_nickname = "", account_number = 0, account_type = "", balance = "20", currency_code = "", isFavorite = 1),
            Account(id = "ccc", account_nickname = null, account_number = 0, account_type = "", balance = "30", currency_code = "", isFavorite = 0)
        )

        val result = refreshAccounts(accountDtoList, accountList)
        assertThat(result[0].id == "aaa").isTrue()
        assertThat(result[0].account_nickname == null).isTrue()
        assertThat(result[0].account_number == 10).isTrue()
        assertThat(result[0].account_type == "t").isTrue()
        assertThat(result[0].balance == "0").isTrue()
        assertThat(result[0].isFavorite == 0).isTrue()

        assertThat(result[1].id == "bbb").isTrue()
        assertThat(result[1].account_nickname != null).isTrue()
        assertThat(result[1].account_number == 20).isTrue()
        assertThat(result[1].balance == "0").isTrue()
        assertThat(result[1].isFavorite == 1).isTrue()

        assertThat(result[2].id == "ccc").isTrue()
        assertThat(result[2].account_nickname != null && result[2].account_nickname == "ccc").isTrue()
        assertThat(result[2].account_number == 30).isTrue()
        assertThat(result[2].balance == "0").isTrue()
        assertThat(result[2].isFavorite == 0).isTrue()
    }

}