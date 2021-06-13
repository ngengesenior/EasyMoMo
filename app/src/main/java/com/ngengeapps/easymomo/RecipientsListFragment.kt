package com.ngengeapps.easymomo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.ngengeapps.easymomo.models.Account
import com.ngengeapps.easymomo.viewmodels.AccountListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecipientsListFragment : Fragment() {

    private val accountsViewModel: AccountListViewModel by viewModels()

    @ExperimentalAnimationApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_recipients_list,
            container,false).apply {
                findViewById<ComposeView>(R.id.navigation_receivers).setContent {
                    MaterialTheme {
                        RecipientList(accountsViewModel)
                    }
                }
        }

    }

    @ExperimentalAnimationApi
    @Composable
    fun RecipientList(accountViewModel: AccountListViewModel = viewModel()) {

        val accounts by accountViewModel.accounts.observeAsState(listOf())

        Scaffold(floatingActionButton = {
            ExtendedFloatingActionButton(text = { Text(text = stringResource(R.string.add_contact)) },onClick = {
                val action = RecipientsListFragmentDirections.actionNavigationReceiversToNavigationAddRecipient()
                findNavController().navigate(action)
            },modifier = Modifier.padding(bottom = 56.dp),
            backgroundColor = Color(0xFFE30425),contentColor = androidx.compose.ui.graphics.Color.White )
        }) {

            AnimatedVisibility(visible = accounts.isNotEmpty()) {
                LazyColumn{
                    items(accounts,key = { it}) { account ->
                        ContactItem(account = account)
                        Spacer(modifier = Modifier.height(4.dp))
                    }


                }
            }
            AnimatedVisibility(visible = accounts.isEmpty()) {
                NoContacts()
            }


        }




    }



    @Composable
    fun ContactItem(account: Account) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 6.dp),elevation = 4.dp) {
            Row( modifier = Modifier.clickable {
                val action =
                    RecipientsListFragmentDirections.actionNavigationReceiversToNavigationTransfer(account.trimAccount())
                findNavController().navigate(action)

            } ,verticalAlignment = Alignment.CenterVertically) {
                Column{
                    Text(text = account.name)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = account.number)
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(painter = painterResource(R.drawable.ic_attach_money_24), contentDescription =null)

            }

        }
    }

}

@Composable
fun NoContacts() {
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()) {
        Text(text = stringResource(R.string.no_contacts))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(R.string.add_fav_prompt),
            modifier = Modifier.padding(16.dp),fontWeight = FontWeight.Bold)

    }
}


