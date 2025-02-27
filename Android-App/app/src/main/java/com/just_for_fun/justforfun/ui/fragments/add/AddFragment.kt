package com.just_for_fun.justforfun.ui.fragments.add

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.databinding.FragmentAddBinding
import com.just_for_fun.justforfun.databinding.FragmentHomeBinding
import com.just_for_fun.justforfun.ui.fragments.home.HomeViewModel

class AddFragment : Fragment(R.layout.fragment_add) {

    private lateinit var binding: FragmentAddBinding
    private val viewModel: AddViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddBinding.bind(view)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

}