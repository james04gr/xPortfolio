package com.xecoding.portfolio.ui.account_list

import androidx.recyclerview.widget.DiffUtil
import com.xecoding.portfolio.data.persistent.Account

class AccountsDiffUtilCallback(
    private val oldList: List<Account>,
    private val newList: List<Account>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.account_nickname == newItem.account_nickname
                && oldItem.balance == newItem.balance
                && oldItem.account_type == newItem.account_type
                && oldItem.isFavorite == newItem.isFavorite
    }

}