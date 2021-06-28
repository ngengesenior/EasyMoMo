package com.ngengeapps.easymomo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ngengeapps.easymomo.viewmodels.QRViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.ngengeapps.easymomo.R
import com.ngengeapps.easymomo.utils.MyAppBar

@AndroidEntryPoint
class MyQRCodeFragment : Fragment() {
    private val qrViewModel:QRViewModel by viewModels()

    @ExperimentalAnimationApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(context =container!!.context).apply {
            id = R.id.navigation_my_qr
            setContent {
                Scaffold(topBar = {
                    MyAppBar(title = stringResource(id = R.string.receive_money))
                }) {
                    QRView(qrViewModel)
                }
                
            }
        }



    }
}


@ExperimentalAnimationApi
@Composable
fun QRView(qrViewModel: QRViewModel =  viewModel()) {
    val bitmap by qrViewModel.bitmap.observeAsState()
    Column(verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.fillMaxSize()) {

        AnimatedVisibility(visible = bitmap != null) {
            bitmap?.let {
                Image(it.asImageBitmap(), contentDescription = null)
            }
        }

    }

}

