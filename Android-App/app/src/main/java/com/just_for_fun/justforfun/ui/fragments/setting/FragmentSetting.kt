package com.just_for_fun.justforfun.ui.fragments.setting

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.just_for_fun.justforfun.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.just_for_fun.justforfun.databinding.FragmentSettingBinding
import com.just_for_fun.justforfun.util.delegates.viewBinding

class FragmentSetting : Fragment(R.layout.fragment_setting) {

    private val binding by viewBinding(FragmentSettingBinding::bind)
    private val viewModel: SettingViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val activity = requireActivity() as AppCompatActivity
        val toolbar = binding.toolbar
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.settings_container, SettingsFragment())
                .commit()
        }
    }
}

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)
    }
}
