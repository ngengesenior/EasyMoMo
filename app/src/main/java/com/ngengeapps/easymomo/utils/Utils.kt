package com.ngengeapps.easymomo.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ngengeapps.easymomo.R
import com.ngengeapps.easymomo.models.PhoneType

@Composable
fun MyAppBar(title: String = "") {
    TopAppBar(title = {
        Text(text = title, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }, backgroundColor = colorResource(R.color.colorPrimary), contentColor = Color.White)
}


@Composable
fun TextFieldTranparent() = TextFieldDefaults.textFieldColors(
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent
)

@Composable
fun BigHeader(title: String) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(100.dp)
    ) {
        drawRect(
            Brush.linearGradient(colors = listOf(Color.Green, Color.Blue))
        )
        drawArc(
            color = Color.White,
            startAngle = 0f,
            sweepAngle = 60f,
            size = Size(size.width * 0.02f, size.height * 0.02f),
            useCenter = false
        )

    }
}

@Composable
fun FancyAppBar(title: String) {
    Card(
        shape = RoundedCornerShape(bottomStart = 40.dp), modifier = Modifier.height(150.dp),
        backgroundColor = colorResource(R.color.colorPrimary)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Bottom
        ) {
            Text(text = title)
        }
    }

}


fun getNumberType(number: String): PhoneType {
    if (
        number.startsWith("67")
        || number.startsWith("650")
        || number.startsWith("651")
        || number.startsWith("652")
        || number.startsWith("653")
        || number.startsWith("654")
        || number.startsWith("680")
    ) {
        return PhoneType.MTN
    } else if (
        number.startsWith("69") ||
        number.startsWith("655")
        || number.startsWith("656")
        || number.startsWith("657")
        || number.startsWith("658")
        || number.startsWith("659")
    ) {
        return PhoneType.ORANGE
    }
    return PhoneType.NOT_VALID
}

fun Context.dialUSSD(number: String,amount:String) {
    when(getNumberType(number)) {
        PhoneType.MTN -> {
            startActivity(Intent(
                Intent.ACTION_CALL,
                Uri.fromParts(
                    "tel",
                    "*126*9*$number*$amount*1#",
                    null
                )
            ))
        }

        PhoneType.ORANGE -> {

            startActivity(Intent(Intent.ACTION_CALL,
                Uri.fromParts("tel",
                    "#150*1*1*$number*$amount#",
                    null
                )
            ))
        }
        else -> Toast.makeText(this,getString(R.string.valid_number_required),Toast.LENGTH_LONG).show()
    }
}



fun calculateMTNMoMoChargers(amount:Int):Int{

    return when(amount){
        in (0..99) -> 0
        in (100..5999) -> (0.03 * amount.toDouble()).toInt()
        in (6_000..10_050) -> 175
        in (10_051..13_550) -> 300
        in (13_551..25_050) -> 350
        in (25_051..50_050) -> 700
        in (50_051..75_100) -> 1350
        in (75_101..100_100) -> 1800
        in (100_101..200_500) -> 2150
        in (200_501..300_500) -> 2600
        in (300_501..400_500) -> 3100
        in (400_501..500_000) -> 3500
        else -> 3500 // Not available, we use default
    }

}

fun calculateOrangeMoneyCharges(amount: Int):Int {
    return when(amount) {
        in (0..6500)-> (0.03 * amount.toDouble()).toInt()
        in (6_501..10_000) -> 180
        in (10_001..13_500) -> 300
        in (13_501..25_000) -> 350
        in (25_001..50_000) -> 700
        in (50_001..80_000) -> 1350
        in (80_001..100_000) -> 1800
        in (100_101..200_000) -> 2150
        in (200_001..300_000) -> 2600
        in (300_101..400_000) -> 3100
        in (400_001..500_000) -> 3600
        else -> 3600 // Not available, we use default
    }
}


data class Page(val title: String, val description: String,@DrawableRes val image:Int)

val onboardPages = listOf<Page>(
    Page(
        "Easy Momo Transfer",
        "Make a quick transaction with someone besides you by scanning their Easy Momo Code",
        R.drawable.ic_scan
    ),
    Page(
        "Easy Select",
        "Pick a contact directly from your contact list and Momo them in less than no time",
        R.drawable.ic_select
    ),
    Page(
        "Secure",
        "We will never know your Momo PIN in anyway.The app does not share your data with any third party. No internet connection required",
        R.drawable.ic_security
    )
)