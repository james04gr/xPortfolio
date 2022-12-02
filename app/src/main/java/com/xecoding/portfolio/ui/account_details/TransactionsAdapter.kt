package com.xecoding.portfolio.ui.account_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.xecoding.portfolio.R
import com.xecoding.portfolio.common.Params.VIEW_TYPE_DATE_SEPARATOR_ITEM
import com.xecoding.portfolio.common.Params.VIEW_TYPE_TRANSACTION_ITEM
import com.xecoding.portfolio.data.remote.dto.Transaction
import com.xecoding.portfolio.databinding.DateSeparatorItemBinding
import com.xecoding.portfolio.databinding.TransactionListItemBinding

class TransactionsAdapter : PagingDataAdapter<TransactionUi, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TransactionUi>() {
            override fun areItemsTheSame(oldItem: TransactionUi, newItem: TransactionUi): Boolean =
                if (oldItem is TransactionUi.DateItem && newItem is TransactionUi.DateItem) true
                else if (oldItem is TransactionUi.TransactionItem && newItem is TransactionUi.TransactionItem)
                    oldItem.transaction.id == newItem.transaction.id
                else true


            override fun areContentsTheSame(
                oldItem: TransactionUi,
                newItem: TransactionUi
            ): Boolean =
                if (oldItem is TransactionUi.DateItem && newItem is TransactionUi.DateItem) true
                else if (oldItem is TransactionUi.TransactionItem && newItem is TransactionUi.TransactionItem)
                    oldItem.transaction.date == newItem.transaction.date
                            && oldItem.transaction.transaction_amount == newItem.transaction.transaction_amount
                            && oldItem.transaction.is_debit == newItem.transaction.is_debit
                            && oldItem.transaction.transaction_type == newItem.transaction.transaction_type
                else true
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.let { item ->
            when (item) {
                is TransactionUi.DateItem -> VIEW_TYPE_DATE_SEPARATOR_ITEM
                is TransactionUi.TransactionItem -> VIEW_TYPE_TRANSACTION_ITEM
            }
        } ?: VIEW_TYPE_TRANSACTION_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_DATE_SEPARATOR_ITEM -> DateSeparatorViewHolder.from(parent)
            VIEW_TYPE_TRANSACTION_ITEM -> TransactionViewHolder.from(parent)
            else -> TransactionViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_DATE_SEPARATOR_ITEM -> (holder as DateSeparatorViewHolder).bind(
                (getItem(position)!! as TransactionUi.DateItem).newDate
            )
            VIEW_TYPE_TRANSACTION_ITEM -> (holder as TransactionViewHolder).bind(
                (getItem(position)!! as TransactionUi.TransactionItem).transaction
            )
        }
    }



    // Different Layouts for Transaction and DateSeparator
    class TransactionViewHolder internal constructor(private val binding: TransactionListItemBinding) : RecyclerView.ViewHolder(binding.root) {
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


    class DateSeparatorViewHolder internal constructor(private val binding: DateSeparatorItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(date: String) {
            binding.dateSeparator.text = date
        }

        companion object {
            fun from(parent: ViewGroup): DateSeparatorViewHolder = DateSeparatorViewHolder(
                DateSeparatorItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

}