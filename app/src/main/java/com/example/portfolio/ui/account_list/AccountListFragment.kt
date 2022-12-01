package com.example.portfolio.ui.account_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.portfolio.R
import com.example.portfolio.common.toast
import com.example.portfolio.data.remote.dto.AccountDto
import com.example.portfolio.databinding.FragmentAccountListBinding
import com.example.portfolio.ui.MainViewModel
import com.example.portfolio.ui.account_details.AccountDetailsFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AccountListFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentAccountListBinding
    private lateinit var viewAdapter: AccountsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewManager = LinearLayoutManager(requireContext())
        viewAdapter = AccountsAdapter(object: AccountListItemClicked {
            override fun onAccountClicked(accountDto: AccountDto) {
                navigateToAccountDetails(accountDto)
            }
        })

        binding.accountsRecycler.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            // Below block is executed when lifecycle is at least STARTED and is cancelled when lifecycle is STOPPED
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Safely collect from mainViewModel.accounts when the lifecycle is STARTED
                mainViewModel.accounts.collectLatest {
                    println(it)
                    updateViews(it)
                }
            }
        }
    }

    private fun updateViews(state: AccountListState) {
        binding.loadingGroup.visibility = if (state.isLoading) View.VISIBLE else View.GONE

        binding.accountsRecycler.visibility = if (state.isLoading) View.GONE else View.VISIBLE
        viewAdapter.updateItems(state.accounts)

        state.error?.let {
            binding.errorText.text = it
            binding.errorText.visibility = View.VISIBLE
        } ?: run {
            binding.errorText.text = ""
            binding.errorText.visibility = View.GONE
        }
    }

    private fun navigateToAccountDetails(accountDto: AccountDto) {
        if (accountDto.id.isEmpty()) {
            toast("Account id is empty")
            return
        }

        mainViewModel.setSelectedAccountId(accountDto.id)
        mainViewModel.getAccountDetails(accountDto.id)
        val bundle = bundleOf(AccountDetailsFragment.ACCOUNT_ID to accountDto.id)
        findNavController().navigate(R.id.action_accountListFragment_to_accountDetailsFragment, bundle)
    }

}