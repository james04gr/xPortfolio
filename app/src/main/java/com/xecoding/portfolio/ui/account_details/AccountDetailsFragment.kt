package com.xecoding.portfolio.ui.account_details

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.xecoding.portfolio.databinding.FragmentAccountDetailsBinding
import com.xecoding.portfolio.common.asHtml
import com.xecoding.portfolio.data.remote.dto.AccountDto
import com.xecoding.portfolio.ui.MainViewModel
import com.xecoding.portfolio.ui.account_list.AccountListItemClicked
import com.xecoding.portfolio.ui.account_list.AccountsAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AccountDetailsFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentAccountDetailsBinding
    private lateinit var viewAdapter: TransactionsAdapter
    private var currentAccountId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewManager = LinearLayoutManager(requireContext())
        viewAdapter = TransactionsAdapter()

        binding.transactions.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            // Below block is executed when lifecycle is at least STARTED and is cancelled when lifecycle is STOPPED
            // Each collection needs a separate coroutine since its a blocking function
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mainViewModel.selectedAccount.collectLatest {
                        it.firstOrNull()?.let { account ->
                            updateSelectedAccountViews(account)
                            currentAccountId = account.id
                        }
                    }
                }

                launch {
                    mainViewModel.accountDetails.collectLatest {
                        updateAccountDetailsViews(it)
                    }
                }

                launch {
                    mainViewModel.transactionsFlow.collectLatest {
                        viewAdapter.submitData(it)
                    }
                }
            }
        }

//        mainViewModel.setInputSource(currentAccountId, "", "")
//        mainViewModel.setInputSource(currentAccountId, "2018-01-16T13:15:30+03:00", "2018-05-16T13:15:30+03:00")
    }

    private fun updateSelectedAccountViews(account: AccountDto) {
        binding.account.nickname.text =
            if (TextUtils.isEmpty(account.account_nickname)) account.account_number.toString()
            else account.account_nickname
        binding.account.balance.text = account.balance + " " + account.currency_code
        binding.account.type.text = account.account_type
        binding.details.detailsType.text = "<b>Type:</b> ${account.account_type}".asHtml()
    }

    private fun updateAccountDetailsViews(state: AccountDetailsState) {
        binding.details.detailsLoading.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        binding.details.detailsGroup.visibility = if (state.isLoading) View.INVISIBLE else View.VISIBLE

        state.accountDetails?.let {
            binding.details.apply {
                productName.text = "<b>Product name:</b> ${it.product_name}".asHtml()
                openedDate.text = "<b>Opened date:</b> ${it.opened_date}".asHtml()
                branch.text = "<b>Branch:</b> ${it.branch}".asHtml()
                beneficiaries.text = "<b>Beneficiaries:</b> ${it.beneficiaries.joinToString(separator = ", ")}".asHtml()
            }
        }

        binding.details.root.visibility = if (state.error == null) View.VISIBLE else View.GONE
    }

    companion object {
        const val ACCOUNT_ID = "account_id"
    }

}