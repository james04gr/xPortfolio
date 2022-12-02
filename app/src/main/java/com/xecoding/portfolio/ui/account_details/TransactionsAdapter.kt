package com.xecoding.portfolio.ui.account_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.xecoding.portfolio.R
import com.xecoding.portfolio.data.remote.dto.Transaction
import com.xecoding.portfolio.databinding.TransactionListItemBinding

class TransactionsAdapter :
    PagingDataAdapter<Transaction, TransactionsAdapter.TransactionViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean =
                oldItem.date == newItem.date && oldItem.transaction_amount == newItem.transaction_amount
                        && oldItem.is_debit == newItem.is_debit && oldItem.transaction_type == newItem.transaction_type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder =
        TransactionViewHolder.from(parent)

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class TransactionViewHolder internal constructor(private val binding: TransactionListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            binding.type.text = transaction.transaction_type
            binding.amount.apply {
                text = transaction.transaction_amount
                setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        if (transaction.is_debit) R.color.colorError else R.color.colorSuccess
                    )
                )
            }
            binding.description.text = transaction.description ?: ""
        }

        companion object {
            fun from(parent: ViewGroup): TransactionViewHolder = TransactionViewHolder(
                TransactionListItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }


}