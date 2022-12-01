package com.xecoding.portfolio.ui.account_list

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.xecoding.portfolio.data.remote.dto.AccountDto
import com.xecoding.portfolio.databinding.AccountListItemBinding

class AccountsAdapter(
    private val onItemClicked: AccountListItemClicked
): RecyclerView.Adapter<AccountsAdapter.AccountViewHolder>() {

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
        holder.bind(accounts[position], onItemClicked)

    override fun getItemCount(): Int = accounts.size

    class AccountViewHolder internal constructor(private val binding: AccountListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(account: AccountDto, onItemClicked: AccountListItemClicked) {
            binding.nickname.text =
                if (TextUtils.isEmpty(account.account_nickname)) account.account_number.toString()
                else account.account_nickname
            binding.balance.text = account.balance + " " + account.currency_code
            binding.type.text = account.account_type

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