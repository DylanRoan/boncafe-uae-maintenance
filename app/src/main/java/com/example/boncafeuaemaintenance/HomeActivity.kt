package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_home.*


@Suppress("DEPRECATION", "UNUSED_EXPRESSION")
class HomeActivity : AppCompatActivity() {
    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Navigation Controller (for controlling fragments/pages)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        // Make a functional BottomNavMenu
        bottomNavigationView.setupWithNavController(navController)

        // Set up toolbar (top nav menu)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false) // disable default toolbar's title

        // Click menu items listener for BottomNavMenu
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->

            // Change Toolbar's title depending the item selected
            toolbar_title.text = when(menuItem.itemId) {
                R.id.home -> "Home"
                R.id.booking -> "Booking"
                R.id.maintenance -> "Maintenance"
                R.id.serviceReport -> "Reports"
                R.id.settings -> "Settings"
                else -> null
            }
            true

            // To update pages
            NavigationUI.onNavDestinationSelected(menuItem, navController)
        }
    }
}