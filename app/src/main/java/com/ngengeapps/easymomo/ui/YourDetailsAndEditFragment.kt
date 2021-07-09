package com.ngengeapps.easymomo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ngengeapps.easymomo.R
import com.ngengeapps.easymomo.models.TrimmedAccount
import com.ngengeapps.easymomo.utils.MyAppBar
import com.ngengeapps.easymomo.viewmodels.YourDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YourDetailsAndEditFragment : Fragment() {

    @ExperimentalAnimationApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return ComposeView(requireContext()).apply {
            id = R.id.navigation_your_details
            setContent {
                Scaffold(
                    topBar = {
                        MyAppBar(title = stringResource(R.string.your_details))
                    }
                ) {
                    YourDetailsAndEditScreen()

                }

            }
        }
    }


}


@ExperimentalAnimationApi
@Composable
fun YourDetailsAndEditScreen(yourDetailsViewModel: YourDetailsViewModel = viewModel()) {

    val context = LocalContext.current
    val name by yourDetailsViewModel.name.observeAsState("")
    val number by yourDetailsViewModel.number.observeAsState("")
    val isEditing by yourDetailsViewModel.isEditing.observeAsState(false)
    val errorMessage by yourDetailsViewModel.errorMessage.observeAsState("")
    val account by yourDetailsViewModel.account.observeAsState()
    LaunchedEffect(errorMessage) {
        if (errorMessage.trim().isNotBlank()) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        floatingActionButton = {
            if (!isEditing) {
                FloatingActionButton(onClick = {
                    yourDetailsViewModel.updateIsEditing(true)
                }, modifier = Modifier.padding(bottom = 56.dp)) {

                    Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)

                }
            }
        }
    ) {

        AnimatedVisibility(visible = (!isEditing && account != null)) {

            YourDetails(trimmedAccount = TrimmedAccount(name.trim(), number.trim()))

        }

        AnimatedVisibility(visible = isEditing) {
            NameAndPhoneScreen(
                name = name,
                phone = number,
                onNameChange = { yourDetailsViewModel.updateName(it) },
                onPhoneChange = { yourDetailsViewModel.updateNumber(it) }) {

                yourDetailsViewModel.updateYourDetails()

            }

        }

    }


}


@Composable
fun YourDetails(trimmedAccount: TrimmedAccount) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Icon(Icons.Rounded.Person,
            contentDescription =null,
            tint = colorResource(R.color.colorPrimary) )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = trimmedAccount.name,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp, fontStyle = FontStyle.Italic
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = trimmedAccount.number,
            fontWeight = FontWeight.Medium,
            fontSize = 17.sp
        )

    }
}