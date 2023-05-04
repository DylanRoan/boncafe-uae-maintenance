package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.connection_error
import kotlinx.android.synthetic.main.activity_main.*


@Suppress("DEPRECATION", "UNUSED_EXPRESSION")
class HomeActivity : AppCompatActivity() {
    @SuppressLint("InflateParams", "SetTextI18n")

    var hasNetwork = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Check if has wifi or not
        val connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this) { isNetwork ->
            hasNetwork = isNetwork

            val params = connection_error.layoutParams
            if (hasNetwork) params.height = 0
            else params.height = 80

            connection_error.layoutParams = params
        }

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
            for (i in 0 until bottomNavigationView.menu.size()){
                val item = bottomNavigationView.menu.getItem(i)
                if (item.itemId == menuItem.itemId){
                    toolbar_title.text = item.toString()
                }
            }

            // To update pages
            NavigationUI.onNavDestinationSelected(menuItem, navController)
        }
    }
}