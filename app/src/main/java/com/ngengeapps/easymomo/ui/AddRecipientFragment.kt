package com.ngengeapps.easymomo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ngengeapps.easymomo.R
import com.ngengeapps.easymomo.models.Account
import com.ngengeapps.easymomo.utils.MyAppBar
import com.ngengeapps.easymomo.viewmodels.AccountListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRecipientFragment : Fragment() {
    private val accountsViewModel: AccountListViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment


        return ComposeView(requireContext()).apply {
            id = R.id.navigation_add_recipient
            setContent {
                Scaffold(
                    topBar = {
                        MyAppBar(title = stringResource(id = R.string.add_recipient))
                    }
                ) {
                    AddRecipient(accountsViewModel)
                }
            }
        }

        /*return inflater.inflate(
            R.layout.fragment_add_recipient,
            container, false
        ).apply {
            findViewById<ComposeView>(R.id.add_recipient).setContent {
                Scaffold( 
                    topBar = {
                        MyAppBar(title = stringResource(id = R.string.add_recipient))
                    }
                ) {
                    AddRecipient(accountsViewModel)
                }
                

            }
        }*/

    }


    @Composable
    fun AddRecipient(viewModel: AccountListViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

        var name by rememberSaveable {
            mutableStateOf("")
        }
        var number by rememberSaveable {
            mutableStateOf("")
        }

        NameAndPhoneScreen(
            name = name,
            phone = number,
            onNameChange = {
                name = it
            },
            onPhoneChange = { number = it }) {
            val nm = name.trim()
            val num = number.trim()
            if (name.isNotBlank() && number.isNotEmpty()) {
                viewModel.insert(Account(name = nm, number = num))
                val action =
                    AddRecipientFragmentDirections.actionNavigationAddRecipientToNavigationMyQr()
                findNavController().navigate(action)

            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.fields_required),
                    Toast.LENGTH_LONG
                ).show()
            }


        }

    }


}


@Composable
fun NameAndPhoneScreen(
    name: String,
    phone: String,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier
        .padding(horizontal = 6.dp)
        .fillMaxHeight(),verticalArrangement = Arrangement.Center) {

        TextField(value = name, onValueChange = onNameChange, label = {
            Text(text = stringResource(R.string.name))
        }, singleLine = true, modifier = Modifier.fillMaxWidth(),
            trailingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            shape = RoundedCornerShape(12.dp))

        Spacer(modifier = Modifier.height(16.dp))


        TextField(value = phone,
            onValueChange = onPhoneChange,
            label = {
                stringResource(R.string.phone)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            placeholder = {
                Text(text = stringResource(R.string.phone))
            },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            shape = RoundedCornerShape(12.dp))


        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onSaveClick, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = buttonColors(Color(0xFF00796B))
        ) {
            Text(text = stringResource(R.string.save), color = Color.White)
        }

    }


}

