package com.ngengeapps.easymomo.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ngengeapps.easymomo.R
import com.ngengeapps.easymomo.utils.FancyAppBar
import com.ngengeapps.easymomo.utils.MyAppBar
import com.ngengeapps.easymomo.viewmodels.YourDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetDetailsActivity : AppCompatActivity() {

    private val yourDetailsViewModel:YourDetailsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        supportActionBar?.title = getString(R.string.set_details)

        setContent {
            Scaffold(topBar = {
                MyAppBar(title = stringResource(R.string.your_details))
            }) {
                YourDetailsScreen(yourDetailsViewModel)
            }


        }

    }


    @Composable
    fun YourDetailsScreen(yourDetailsViewModel: YourDetailsViewModel = viewModel(),) {
        val name by yourDetailsViewModel.name.observeAsState("")
        val number by yourDetailsViewModel.number.observeAsState("")

        Column(modifier = Modifier.fillMaxSize()) {
            NameAndPhoneScreen(
                name = name ,
                phone = number,
                onNameChange = {
                    yourDetailsViewModel.updateName(it)
                },
                onPhoneChange = { yourDetailsViewModel.updateNumber(it) }) {


                val nm = name.trim()
                val num = number.trim()
                if (nm.isNotEmpty() && num.isNotEmpty()) {

                    yourDetailsViewModel.updateYourDetails()
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

}

