package com.example.portfolio.ui.account_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.portfolio.databinding.FragmentAccountDetailsBinding


class AccountDetailsFragment : Fragment() {

    private lateinit var binding: FragmentAccountDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

}