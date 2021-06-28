package com.ngengeapps.easymomo.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.ngengeapps.easymomo.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        val contactString = preferences.getString("MY_ACCOUNT",null)
        if (contactString != null) {
            Handler(Looper.getMainLooper())
                .postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                },1000)

        } else {
            val needToShowOnboarding = preferences.getBoolean("ONBOARD",true)
            if (needToShowOnboarding) {
                Handler(Looper.getMainLooper())
                    .postDelayed({
                        startActivity(Intent(this,OnboardingActivity::class.java))
                        finish()
                    },1000)

            } else {
                Handler(Looper.getMainLooper())
                    .postDelayed({
                        startActivity(Intent(this, SetDetailsActivity::class.java))
                        finish()
                    },1000)
            }


        }


    }
}