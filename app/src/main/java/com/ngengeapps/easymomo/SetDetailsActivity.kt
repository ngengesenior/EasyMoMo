package com.ngengeapps.easymomo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.ngengeapps.easymomo.databinding.ActivitySetDetailsBinding
import com.ngengeapps.easymomo.models.TrimmedAccount

class SetDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySetDetailsBinding.inflate(layoutInflater)
        supportActionBar?.title = getString(R.string.set_details)
        val preferences = getSharedPreferences("my_account", Context.MODE_PRIVATE)
        binding.buttonSave.setOnClickListener {
            binding.apply {
                val name = editTextName.text.toString().trim()
                val number = editTextPhone.text.toString().trim()
                if (name.isNotEmpty() && number.isNotEmpty()) {

                    val gson = Gson()
                    val account = TrimmedAccount(name, number)
                    with(preferences.edit()) {
                        putString("MY_ACCOUNT", gson.toJson(account,TrimmedAccount::class.java))
                        commit()

                    }
                    startActivity(Intent(this@SetDetailsActivity, MainActivity::class.java))
                    finish()

                } else {
                    Toast.makeText(
                        this@SetDetailsActivity,
                        getString(R.string.set_valid_details),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        setContentView(binding.root)
    }
}