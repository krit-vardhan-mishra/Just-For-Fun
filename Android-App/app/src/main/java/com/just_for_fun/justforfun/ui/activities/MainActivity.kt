package com.just_for_fun.justforfun.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.databinding.ActivityMainBinding
import androidx.core.view.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.just_for_fun.justforfun.util.delegates.viewBinding

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val viewModel: MainViewModel by viewModel()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener(this)

        setupBottomNavigation()

        binding.bottomNavigationView.background = null

        binding.bottomNavigationView.menu[2].isEnabled = false

        binding.fab.setOnClickListener {
            navController.navigate(R.id.nav_graph_addFragment)
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_homeFragment -> {
                    navController.navigate(R.id.nav_homeFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_searchFragment -> {
                    navController.navigate(R.id.nav_searchFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_notificationFragment -> {
                    navController.navigate(R.id.nav_notificationFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.nav_accountFragment -> {
                    navController.navigate(R.id.nav_accountFragment)
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        when (destination.id) {
            R.id.nav_homeFragment -> binding.bottomNavigationView.menu.findItem(R.id.nav_homeFragment).isChecked = true
            R.id.nav_searchFragment -> binding.bottomNavigationView.menu.findItem(R.id.nav_searchFragment).isChecked = true
            R.id.nav_notificationFragment -> binding.bottomNavigationView.menu.findItem(R.id.nav_notificationFragment).isChecked = true
            R.id.nav_accountFragment -> binding.bottomNavigationView.menu.findItem(R.id.nav_accountFragment).isChecked = true
            R.id.nav_graph_addFragment -> binding.bottomNavigationView.menu[2].isChecked = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        navController.removeOnDestinationChangedListener(this)
    }
}