package com.ngengeapps.easymomo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import com.ngengeapps.easymomo.models.TrimmedAccount
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetDetailsActivity : AppCompatActivity() {
    private val gson = Gson()
    private val preferences:SharedPreferences by lazy {
        getSharedPreferences("my_account", Context.MODE_PRIVATE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = getString(R.string.set_details)

        setContent {
            YourDetailsScreen()

        }
        /*val binding = ActivitySetDetailsBinding.inflate(layoutInflater)
        supportActionBar?.title = getString(R.string.set_details)
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
        setContentView(binding.root)*/
    }


    @Composable
    fun YourDetailsScreen() {
        var name by rememberSaveable {
            mutableStateOf("")
        }
        var number by rememberSaveable{
            mutableStateOf("")
        }
        NameAndPhoneScreen(
            name = name ,
            phone = number,
            onNameChange = {
                           name = it
            },
            onPhoneChange = { number = it }) {


            val nm = name.trim()
            val num = number.trim()
            if (nm.isNotEmpty() && num.isNotEmpty()) {


                val account = TrimmedAccount(name, number)
                with(preferences.edit()) {
                    putString("MY_ACCOUNT", gson.toJson(account,
                        TrimmedAccount::class.java))
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

}

