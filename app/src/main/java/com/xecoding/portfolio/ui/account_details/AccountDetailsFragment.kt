package com.xecoding.portfolio.ui.account_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.xecoding.portfolio.R
import com.xecoding.portfolio.common.*
import com.xecoding.portfolio.data.persistent.Account
import com.xecoding.portfolio.data.remote.dto.AccountDetailsDto
import com.xecoding.portfolio.data.remote.dto.AccountDto
import com.xecoding.portfolio.databinding.FragmentAccountDetailsBinding
import com.xecoding.portfolio.ui.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

class AccountDetailsFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentAccountDetailsBinding
    private lateinit var viewAdapter: TransactionsAdapter
    private lateinit var currentAccount: Account
    private var currentDetails: AccountDetailsDto? = null

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
                    mainViewModel.accountWithDetails.collectLatest {
                        it?.let {
                            currentAccount = it.first
                            currentDetails = it.second.accountDetails
                            requireActivity().title = it.first.displayName()
                            mainViewModel.setIsFavorite(it.first.isFavorite.toBoolean())
                            setUpFavoriteIcon()
                            updateSelectedAccountViews(it.first)
                            updateAccountDetailsViews(it.second)
                        }
                    }
                }

                launch {
                    mainViewModel.transactionsFlow.collectLatest {
                        viewAdapter.submitData(it)
                    }
                }

                launch {
                    // In order to hide the Clear button if Filter has not been applied
                    mainViewModel.isFiltered.collectLatest {
                        binding.clear.visibility = if (it) View.VISIBLE else View.GONE
                    }
                }
            }
        }

        binding.filter.setOnClickListener {
            dateDialog()
        }

        binding.clear.setOnClickListener {
            clearDateFilters()
        }

        binding.account.favorite.setOnClickListener {
            if (!mainViewModel.isFavorite.value) {
                mainViewModel.saveFavorite(currentAccount, currentDetails)
                requireContext().toast("${currentAccount.displayName()} saved as favorite")
            } else {
                mainViewModel.deleteFavorite()
                requireContext().toast("${currentAccount.displayName()} removed from favorite")
            }
            mainViewModel.setIsFavorite(!mainViewModel.isFavorite.value)
            setUpFavoriteIcon()
        }
    }

    private fun updateSelectedAccountViews(account: Account) {
        binding.account.nickname.text = account.displayName()
        binding.account.balance.text = account.amountWithCurrency()
        binding.account.type.text = account.account_type
        binding.details.detailsType.text = "<b>Type:</b> ${account.account_type}".asHtml()
    }

    private fun updateAccountDetailsViews(state: AccountDetailsState) {
        binding.details.detailsLoading.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        binding.details.detailsGroup.visibility = if (state.isLoading) View.INVISIBLE else View.VISIBLE
        binding.account.favorite.visibility = if (state.isLoading) View.INVISIBLE else View.VISIBLE

        state.accountDetails?.let {
            binding.details.apply {
                productName.text = "<b>Product name:</b> ${it.product_name}".asHtml()
                openedDate.text = "<b>Opened date:</b> ${it.opened_date}".asHtml()
                branch.text = "<b>Branch:</b> ${it.branch}".asHtml()
                beneficiaries.text = "<b>Beneficiaries:</b> ${it.beneficiaries.joinToString(separator = ", ")}".asHtml()
            }

            binding.account.favorite.visibility = View.VISIBLE
        } ?: run {
            binding.account.favorite.visibility = View.GONE
            if (!state.isLoading) binding.details.root.visibility = View.GONE
        }

        state.error?.let {
            binding.errorText.text = it
            binding.errorText.visibility = View.VISIBLE
        } ?: run {
            binding.errorText.text = ""
            binding.errorText.visibility = View.GONE
        }
    }

    private fun setUpFavoriteIcon() {
        binding.account.favorite.setImageDrawable(
            ContextCompat.getDrawable(requireContext(),
                if (mainViewModel.isFavorite.value) R.drawable.ic_star_full else R.drawable.ic_star_empty
            )
        )
    }

    private fun dateDialog() {
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker().apply {
                setTitleText("Select dates")
                setCalendarConstraints(CalendarConstraints.Builder().setEnd(System.currentTimeMillis()).build())
            }.build().apply {
                addOnPositiveButtonClickListener { dateRangePicked(it.first, it.second) }
            }
        dateRangePicker.show(childFragmentManager, "")
    }

    private fun dateRangePicked(start: Long, end: Long) {
        viewAdapter.submitData(lifecycle, PagingData.empty())
        mainViewModel.setIsFiltered(true)
        mainViewModel.setInputSource(
            currentAccount.id,
            fromDate = start.formatAs(),
            toDate = end.formatAs())
    }

    private fun clearDateFilters() {
        viewAdapter.submitData(lifecycle, PagingData.empty())
        mainViewModel.setIsFiltered(false)
        mainViewModel.setInputSource(currentAccount.id, "", "")
    }

    companion object {
        const val ACCOUNT_ID = "account_id"
    }

}