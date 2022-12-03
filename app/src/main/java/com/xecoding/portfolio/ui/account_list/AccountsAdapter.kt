package com.xecoding.portfolio.ui.account_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.xecoding.portfolio.R
import com.xecoding.portfolio.common.amountWithCurrency
import com.xecoding.portfolio.common.displayName
import com.xecoding.portfolio.common.toBoolean
import com.xecoding.portfolio.data.persistent.Account
import com.xecoding.portfolio.databinding.AccountListItemBinding

class AccountsAdapter(
    private val onItemClicked: AccountListItemClicked
): RecyclerView.Adapter<AccountsAdapter.AccountViewHolder>() {

    private var accounts = listOf<Account>()

    fun updateItems(newAccounts: List<Account>) {
        val diffCallback = AccountsDiffUtilCallback(this.accounts, newAccounts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        accounts = newAccounts
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder =
        AccountViewHolder.from(parent)

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) =
        holder.bind(accounts[position], onItemClicked)

    override fun getItemCount(): Int = accounts.size

    class AccountViewHolder internal constructor(private val binding: AccountListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(account: Account, onItemClicked: AccountListItemClicked) {
            binding.nickname.text = account.displayName()
            binding.balance.text = account.amountWithCurrency()
            binding.type.text = account.account_type
            binding.favorite.visibility = if (account.isFavorite.toBoolean()) View.VISIBLE else View.GONE
            binding.favorite.isEnabled = false

            itemView.setOnClickListener {
                onItemClicked.onAccountClicked(account)
            }
        }

        companion object {
            fun from(parent: ViewGroup): AccountViewHolder = AccountViewHolder(
                AccountListItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }
}