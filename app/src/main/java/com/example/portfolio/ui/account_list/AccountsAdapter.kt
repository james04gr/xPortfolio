package com.example.portfolio.ui.account_list

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolio.data.remote.dto.AccountDto
import com.example.portfolio.databinding.AccountListItemBinding

class AccountsAdapter : RecyclerView.Adapter<AccountsAdapter.AccountViewHolder>() {

    private var accounts = listOf<AccountDto>()

    fun updateItems(newAccounts: List<AccountDto>) {
        val diffCallback = AccountsDiffUtilCallback(this.accounts, newAccounts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        accounts = newAccounts
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder =
        AccountViewHolder.from(parent)

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) =
        holder.bind(accounts[position])

    override fun getItemCount(): Int = accounts.size

    class AccountViewHolder internal constructor(private val binding: AccountListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(account: AccountDto) {
            binding.nickname.text =
                if (TextUtils.isEmpty(account.account_nickname)) account.account_number.toString()
                else account.account_nickname
            binding.balance.text = account.balance
            binding.type.text = "Type: ${account.account_type}"
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