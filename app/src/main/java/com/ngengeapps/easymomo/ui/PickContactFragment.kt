package com.ngengeapps.easymomo.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ngengeapps.easymomo.R
import com.ngengeapps.easymomo.utils.*

class PickContactFragment : Fragment() {
    lateinit var permission: ActivityResultLauncher<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        permission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        }
    }
    @ExperimentalComposeUiApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return ComposeView(requireContext()).apply {
            id = R.id.navigation_pick_contact
            setContent {
                Scaffold(
                    topBar = {
                        MyAppBar(title = stringResource(id = R.string.pick_contact))
                    }
                ) {

                    PickContactUI()
                }
            }
        }


    }


    @ExperimentalComposeUiApi
    @Composable
    fun PickContactUI() {
        val contactUri = remember {
            mutableStateOf<Uri?>(null)
        }
        val phones by remember {
            mutableStateOf(mutableListOf<String>())
        }
        var key: String? by remember {
            mutableStateOf(null)
        }
        var name: String? by remember {
            mutableStateOf(null)
        }

        var number: String by remember {
            mutableStateOf("")
        }
        var amount: String by remember {
            mutableStateOf("")
        }
        val context = LocalContext.current

        val focusManager = LocalFocusManager.current

        val launchPickContact =
            rememberLauncherForActivityResult(ActivityResultContracts.PickContact()) { uri:Uri? ->
                contactUri.value = uri
                var cursor: Cursor? = null
                uri?.let {

                }
                if (uri != null) { // A contact was selected
                    try {
                        val columns = arrayOf(
                            ContactsContract.Contacts._ID,
                            ContactsContract.Contacts.DISPLAY_NAME
                        )
                        cursor = context.contentResolver.query(uri, columns, null, null, null)
                        if (cursor != null && cursor.moveToFirst()) {
                            key = cursor.getString(0)
                            name = cursor.getString(1)

                            val phoneCursor = context.contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                                arrayOf(key), null
                            )
                            if (phoneCursor != null) {
                                while (phoneCursor.moveToNext()) {
                                    number = phoneCursor.getString(
                                        phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                    )
                                    number = convertToLocalNumber(number)
                                    phones.add(
                                        phoneCursor.getString(
                                            phoneCursor.getColumnIndex(
                                                ContactsContract.CommonDataKinds.Phone.NUMBER
                                            )
                                        )
                                    )
                                }
                            }

                            phoneCursor?.close()

                        }

                    } finally {

                        cursor?.close()
                    }
                }



            }

        val readPhonePermission =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    launchPickContact.launch()

                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                        Toast.makeText(context, getString(R.string.prompt_allow_read_contacts), Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(
                            context,
                            getString(R.string.prompt_open_settings),
                            Toast.LENGTH_LONG
                        ).show()
                        context.openSettings()
                    }
                }

            }



        Column(verticalArrangement = Arrangement.Center,modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)) {
            Text(text = stringResource(R.string.recipient), fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(2.dp))
            Row(modifier = Modifier.padding(horizontal = 2.dp),verticalAlignment = Alignment.CenterVertically) {

                TextField(
                    value = number, onValueChange = {
                        number = it
                    }, modifier = Modifier.fillMaxWidth(), singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    }),trailingIcon = {
                        IconButton(onClick = {
                            readPhonePermission.launch(Manifest.permission.READ_CONTACTS)
                        }) {
                            Icon(Icons.Rounded.Person, contentDescription = number,
                                tint = colorResource(
                                id = R.color.colorPrimary
                            ))

                        }
                    }
                ,shape = RoundedCornerShape(12.dp),colors = TextFieldTranparent())

                IconButton(onClick = {
                    readPhonePermission.launch(Manifest.permission.READ_CONTACTS)
                    focusManager.clearFocus()
                }) {
                    Icon(Icons.Rounded.AccountBox, contentDescription = null)
                }

            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = stringResource(R.string.amount_to_Send), fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(trailingIcon = {
                                     Icon(painter = painterResource(id = R.drawable.ic_money),
                                         contentDescription =null)
            },
                value = amount, onValueChange = {
                    amount = it
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp), singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number,imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()

                })
            ,shape = RoundedCornerShape(12.dp),colors = TextFieldTranparent())

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = {

                    if (amount.isNotEmpty() && number.isNotEmpty()) {

                        if (ContextCompat.checkSelfPermission(requireContext(),
                                Manifest.permission.CALL_PHONE
                            ) == PackageManager.PERMISSION_GRANTED) {

                            requireContext().dialUSSD(number, amount)
                            number = ""
                            amount = ""


                        } else {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                                    Manifest.permission.CALL_PHONE
                                )) {
                                Toast.makeText(requireContext(),getString(R.string.perm_phone),Toast.LENGTH_LONG).show()
                                permission.launch(Manifest.permission.CALL_PHONE)
                            } else {
                                Toast.makeText(requireContext(),getString(R.string.go_to_settings),Toast.LENGTH_LONG).show()
                                requireContext().openSettings()

                            }
                        }

                        permission.launch(Manifest.permission.CALL_PHONE)

                    } else {
                        Toast.makeText(requireContext(), getString(R.string.fields_required),Toast.LENGTH_LONG)
                            .show()
                    }

                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF00796B))
            ,enabled = (amount.isNotEmpty() && number.isNotEmpty())) {
                Text(text = stringResource(R.string.send), color = Color.White)
            }

        }
    }
}
