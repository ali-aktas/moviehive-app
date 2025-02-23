package com.aliaktas.cinematch.ui

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aliaktas.cinematch.R
import com.aliaktas.cinematch.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        window.statusBarColor = Color.TRANSPARENT

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Bottom Navigation ile NavController'ı eşleştir
        binding.bottomNavigationView.setupWithNavController(navController)

        // Aynı fragment'e tıklanırsa yenilemeyi engelle
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            val currentDestination = navController.currentDestination?.id
            if (menuItem.itemId == currentDestination) {
                // Zaten aynı fragment'te olduğumuz için bir şey yapmadan geç
                return@setOnItemSelectedListener true
            }

            // Fragment değiştir
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.searchFragment -> {
                    navController.navigate(R.id.searchFragment)
                    true
                }
                R.id.favoritesFragment -> {
                    navController.navigate(R.id.favoritesFragment)
                    true
                }
                else -> false
            }
        }
    }
}
