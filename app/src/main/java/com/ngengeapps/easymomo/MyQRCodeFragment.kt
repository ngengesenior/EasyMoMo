package com.ngengeapps.easymomo

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ngengeapps.easymomo.utils.GenerateQR
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyQRCodeFragment : Fragment() {


    private val preferences: SharedPreferences by lazy {
        requireActivity().getSharedPreferences("my_account", MODE_PRIVATE)
    }
    private val qrViewModel:QRViewModel by viewModels()

    @ExperimentalAnimationApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.fragment_my_q_r_code,
            container,false).apply {
            findViewById<ComposeView>(R.id.navigation_my_qr).setContent {

                QRView(qrViewModel)

            }
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //generateCode()
    }

    /*private fun generateCode() {
        val details = preferences.getString("MY_ACCOUNT", null)
        if (details != null && details.isNotBlank()) {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                val image = GenerateQR.generateBitmap(
                    details,
                    600,
                    600,
                    4,
                    ContextCompat.getColor(requireContext(), R.color.colorPrimary),
                    Color.parseColor("#FFF5EE")
                )
                withContext(Dispatchers.Main) {
                    binding.imageViewQR.setImageBitmap(image)
                }
            }
        } else {
            Toast.makeText(
                requireActivity(),
                getString(R.string.need_to_set_details),
                Toast.LENGTH_SHORT
            ).show()
        }
        //val details = "%s:%s:%s".format(name,email,phone)


    }*/
}


@ExperimentalAnimationApi
@Composable
fun QRView(qrViewModel: QRViewModel =  viewModel()) {
    val bitmap by qrViewModel.bitmap.observeAsState()
    Column(verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally) {

        AnimatedVisibility(visible = bitmap != null) {
            bitmap?.let {
                Image(it.asImageBitmap(), contentDescription = null)
            }
        }

    }

}

@HiltViewModel
class QRViewModel @Inject constructor( @ApplicationContext val context: Context) :ViewModel(){
    private val _bitmap = MutableLiveData<Bitmap>(null)
    val bitmap:LiveData<Bitmap>
    get() = _bitmap

    init {
        val prefs = context.getSharedPreferences("my_account", MODE_PRIVATE)
        val account = prefs.getString("MY_ACCOUNT",null)
        if (account != null && account.isNotEmpty()) {
            generateBitMap(account)
        }
    }


    private fun generateBitMap(string: String) {
        viewModelScope.launch(Dispatchers.IO) {
           _bitmap.postValue(GenerateQR.generateBitmap(
                string,
                600,
                600,
                4,
                ContextCompat.getColor(context, R.color.colorPrimary),
                Color.parseColor("#FFF5EE")
            ))
        }
    }

}