package com.xecoding.portfolio.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.xecoding.portfolio.R
import com.xecoding.portfolio.data.remote.ConnectivityObserver
import com.xecoding.portfolio.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        lifecycleScope.launchWhenStarted {
            mainViewModel.connectivityObserver.networkState().collectLatest {
            when (it) {
                ConnectivityObserver.ConnectionStatus.Available -> {
                    binding.errorText.apply {
                        isVisible = false
                    }
                }
                ConnectivityObserver.ConnectionStatus.Unavailable -> {
                    binding.errorText.apply {
                        text = "Connection unavailable"
                        background =
                            ContextCompat.getDrawable(
                                this@MainActivity,
                                R.drawable.error_background
                            )
                        isVisible = true
                    }
                }
            }
        }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}