package com.ngengeapps.easymomo.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.textButtonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.ngengeapps.easymomo.viewmodels.QRViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.ngengeapps.easymomo.R
import com.ngengeapps.easymomo.utils.MyAppBar
import com.ngengeapps.easymomo.utils.checkBalance
import com.ngengeapps.easymomo.utils.openSettings

@AndroidEntryPoint
class MyQRCodeFragment : Fragment() {
    private val qrViewModel:QRViewModel by viewModels()
    lateinit var permission: ActivityResultLauncher<String>
    lateinit var reviewManager: ReviewManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        permission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        }
        reviewManager = ReviewManagerFactory.create(requireActivity())
    }


    @ExperimentalAnimationApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        requestForReviews()
        return ComposeView(context =container!!.context).apply {
            id = R.id.navigation_my_qr
            setContent {
                Scaffold(topBar = {
                    MyAppBar(
                        title = stringResource(id = R.string.receive_money),
                        navigationIcon = {
                            Icon(Icons.Rounded.Menu, contentDescription =null,tint = Color.Black )
                        },
                    actions = {
                        TextButton(colors =textButtonColors(contentColor = MaterialTheme.colors.background) ,onClick = {

                            if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                requireContext().checkBalance()
                            } else {
                                if (ActivityCompat.shouldShowRequestPermissionRationale(
                                        requireActivity(),
                                        Manifest.permission.CALL_PHONE
                                    )

                                ) {
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.perm_phone),
                                        Toast.LENGTH_LONG
                                    ).show()
                                    permission.launch(Manifest.permission.CALL_PHONE)
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.go_to_settings),
                                        Toast.LENGTH_LONG
                                    ).show()
                                    requireContext().openSettings()

                                }

                            }


                        },border = BorderStroke(1.dp, colorResource(R.color.colorError))) {
                            Text(text = context.getString(R.string.check_balance))
                        }
                        })
                }) {
                    QRView(qrViewModel)
                }
                
            }
        }



    }


    fun requestForReviews() {
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                val flow = reviewManager.launchReviewFlow(requireActivity(),reviewInfo)
                flow.addOnCompleteListener{ _ ->}
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

