package com.ngengeapps.easymomo.ui

import android.content.Intent
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
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.ngengeapps.easymomo.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val DAYS_FOR_FLEXIBLE_UPDATE = 3
    private val UPDATE_REQUEST_CODE = 100
    private val appUpdateManager:AppUpdateManager by lazy {
        AppUpdateManagerFactory.create(this)
    }

    private lateinit var navController:NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkForUpdates()
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

    private fun checkForUpdates() {
        val appUpdateTaskInfo = appUpdateManager.appUpdateInfo
        val listener = InstallStateUpdatedListener{ state ->

            if (state.installStatus() == InstallStatus.DOWNLOADED){

                popupSnackbarForCompleteUpdate()
            }


        }
        appUpdateManager.registerListener(listener)
        appUpdateTaskInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                 (appUpdateInfo.clientVersionStalenessDays() ?: -1) >= DAYS_FOR_FLEXIBLE_UPDATE

                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.FLEXIBLE,this,UPDATE_REQUEST_CODE)

            } else {
                appUpdateManager.unregisterListener(listener)
            }

        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_REQUEST_CODE) {
            if (resultCode != RESULT_CANCELED) {
                checkForUpdates()

            } else if (resultCode == RESULT_IN_APP_UPDATE_FAILED) {
                checkForUpdates()
            }
        }

    }

    private fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(
            findViewById(R.id.container),
            getString(R.string.update_downloaded),
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(getString(R.string.restart)) { appUpdateManager.completeUpdate() }
            show()
        }
    }

    override fun onResume() {
        super.onResume()

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                // If the update is downloaded but not installed,
                // notify the user to complete the update.
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate()
                }
            }
    }


}