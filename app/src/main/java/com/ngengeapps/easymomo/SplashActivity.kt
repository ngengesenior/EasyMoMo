package com.ngengeapps.easymomo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        val preferences = getSharedPreferences("my_account",Context.MODE_PRIVATE)
        val contactString = preferences.getString("MY_ACCOUNT",null)
        if (contactString != null) {
            Handler(Looper.getMainLooper())
                .postDelayed({
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                },1500)

        } else {
            Handler(Looper.getMainLooper())
                .postDelayed({
                    startActivity(Intent(this,SetDetailsActivity::class.java))
                    finish()
                },1500)

        }


    }
}