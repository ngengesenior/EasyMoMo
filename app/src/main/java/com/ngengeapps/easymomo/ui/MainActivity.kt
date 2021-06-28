package com.ngengeapps.easymomo.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ngengeapps.easymomo.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController:NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_my_qr,
            R.id.navigation_scan,
            R.id.navigation_receivers,
            R.id.navigation_add_recipient,
            R.id.navigation_your_details,
            R.id.navigation_pick_contact
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener {_, destination, _ ->
            showHideNavView(destination.id,navView)
        }
        supportActionBar?.hide()

    }

    private fun showHideNavView(id: Int, navigationView: BottomNavigationView) {
        when (id) {
            R.id.navigation_my_qr,
            R.id.navigation_your_details,
            R.id.navigation_pick_contact,
            R.id.navigation_scan,
            R.id.navigation_receivers -> navigationView.visibility = View.VISIBLE
            else -> navigationView.visibility = View.GONE
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,appBarConfiguration)
    }
}