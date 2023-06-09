package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.connection_error
import kotlinx.android.synthetic.main.activity_main.*


@Suppress("DEPRECATION", "UNUSED_EXPRESSION")
class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
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
            if (hasNetwork){
                params.height = 0

                fragmentContainerView.visibility = View.VISIBLE
                layout_no_wifi.visibility = View.GONE
            }
            else {
                params.height = 80

                fragmentContainerView.visibility = View.GONE
                layout_no_wifi.visibility = View.VISIBLE
            }

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

            // Change Toolbar's title depending on the item selected
            for (i in 0 until bottomNavigationView.menu.size()){
                val item = bottomNavigationView.menu.getItem(i)
                if (item.itemId == menuItem.itemId){
                    toolbar_title.text = item.toString()
                }
            }

            // To update pages
            NavigationUI.onNavDestinationSelected(menuItem, navController)
        }

        // Set up Drawer Menu
        nav_view.setNavigationItemSelectedListener(this)

        // Open Drawer Menu
        icon_menu.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }

        //set header of side view name and email
        val prefName = "com.boncafe_maintenance.app"
        val prefs = getSharedPreferences(prefName, MODE_PRIVATE)
        val name = prefs.getString("$prefName.name", "Full Name")
        val email = prefs.getString("$prefName.email", "username@email.com")

        nav_view.getHeaderView(0).findViewById<TextView>(R.id.txt_fullName).text = name
        nav_view.getHeaderView(0).findViewById<TextView>(R.id.txt_email).text = email

        // Functional social media buttons
        openSocialMedias()
    }



    // For selecting items in Drawer Menu
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.ourWebsite -> CustomFunctions().openURL(this, "https://www.boncafeme.ae/")
            R.id.aboutUs -> CustomFunctions().openURL(this, "https://www.boncafeme.ae/about-us")
            R.id.contactUs -> CustomFunctions().openURL(this, "https://www.boncafeme.ae/contact")
            R.id.serviceMaintenance -> CustomFunctions().openURL(this, "https://www.boncafeme.ae/services")
            R.id.spareParts -> CustomFunctions().openURL(this, "https://www.boncafeme.ae/shop?category=spare-parts&&%20category_name=Spare%20Parts")
            R.id.browseProducts -> CustomFunctions().openURL(this, "https://www.boncafeme.ae/categories")
            R.id.logout -> {
                val prefName = "com.boncafe_maintenance.app"
                val prefs = getSharedPreferences(prefName, MODE_PRIVATE)
                prefs.edit().putString("$prefName.email", "none").apply() //insecure
                prefs.edit().putString("$prefName.password", "none").apply() //insecure
                prefs.edit().putBoolean("$prefName.remember_me", false).apply() //insecure
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        return true
    }

    // Check back press
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            // close drawer if user back press
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    private fun openSocialMedias(){
        // Store icons with their links
        val socialMediaIconsAndLinks = arrayOf(
            arrayOf(icon_facebook, "https://www.facebook.com/boncafeme/"),
            arrayOf(icon_instagram, "https://www.instagram.com/boncafeme/?hl=en"),
            arrayOf(icon_youtube, "https://youtube.com/@boncafemiddleeast5050")
        )

        // Click and open certain social media
        for (social in socialMediaIconsAndLinks){
            val targetIcon = social[0] as ImageView
            val targetLink = social[1] as String

            targetIcon.setOnClickListener {
                CustomFunctions().openURL(this, targetLink)
            }
        }
    }
}