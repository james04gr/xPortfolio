package com.xecoding.portfolio.domain.repository.transactions

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.xecoding.portfolio.data.remote.api.PortfolioApi
import com.xecoding.portfolio.data.remote.dto.FilteredTransactionsRequest
import com.xecoding.portfolio.data.remote.dto.Transaction
import com.xecoding.portfolio.data.remote.dto.TransactionsRequest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private const val TRANSACTIONS_STARTING_PAGE_INDEX = 0

class TransactionsPagingSource(
    var accountId: String, var fromDate: String = "", var toDate: String = ""
) : PagingSource<Int, Transaction>(), KoinComponent {

    private val portfolioApi: PortfolioApi by inject()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Transaction> {
        val nextPageNumber = params.key ?: TRANSACTIONS_STARTING_PAGE_INDEX

        return try {
            val isFiltered = fromDate.isNotEmpty() && toDate.isNotEmpty()
            val transactions = if (isFiltered) portfolioApi.getFilteredTransactions(
                accountId, FilteredTransactionsRequest(
                    from_date = fromDate,
                    next_page = nextPageNumber,
                    to_date = toDate
                )
            )
            else portfolioApi.getTransactions(
                accountId, TransactionsRequest(nextPageNumber)
            )

            LoadResult.Page(
                data = transactions.transactions,
                prevKey = null,
                nextKey = transactions.paging.current_page + 1
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Transaction>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}