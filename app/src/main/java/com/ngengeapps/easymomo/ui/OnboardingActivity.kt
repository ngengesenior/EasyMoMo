package com.ngengeapps.easymomo.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.ngengeapps.easymomo.R
import com.ngengeapps.easymomo.utils.onboardPages
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity:AppCompatActivity() {
    @Inject
    lateinit var preferences: SharedPreferences

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContent {
            Onboarding {
                preferences.edit {
                    putBoolean("ONBOARD", false)
                    commit()
                    startActivity(Intent(this@OnboardingActivity,SetDetailsActivity::class.java))
                    finish()
                }
            }
        }

    }
}


@ExperimentalAnimationApi
@Composable
fun Onboarding(onClick:()->Unit) {
    var progress by remember {
        mutableStateOf(1/3f)
    }

    var currentPage by remember {
        mutableStateOf(0)
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectHorizontalDragGestures { change, dragAmount ->
                if (dragAmount > 0) {
                    if (currentPage > 0) {
                        currentPage -= 1
                        progress -= 1 / 3f
                    }
                } else {

                    if (currentPage < 2) {
                        currentPage += 1
                        progress += 1 / 3f
                    }
                }
                change.consumeAllChanges()


            }
        }

    ) {

        Column(modifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .weight(1f)
            .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))
            Image(painter = painterResource(onboardPages[currentPage].image), contentDescription = null,
                modifier = Modifier.size(200.dp))
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = onboardPages[currentPage].title,
                fontSize = 28.sp,fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = onboardPages[currentPage].description)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier
                    .size(8.dp)
                    .background(color = if (currentPage == 0) colorResource(R.color.colorPrimary) else Color.Gray)

                )
                Box(modifier = Modifier
                    .size(8.dp)
                    .background(color = if (currentPage == 1) colorResource(R.color.colorPrimary) else Color.Gray)

                )

                Box(modifier = Modifier
                    .size(8.dp)
                    .background(color = if (currentPage == 2) colorResource(R.color.colorPrimary) else Color.Gray)

                )
            }
        }

        AnimatedVisibility(visible = currentPage == 2 ) {
            OutlinedButton(shape = RoundedCornerShape(20.dp) ,modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),onClick = onClick,
                colors = ButtonDefaults.outlinedButtonColors(backgroundColor = colorResource(R.color.colorPrimary),contentColor = Color.White)) {
                Text(text = "Get Started")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(progress = progress,
            color = colorResource(R.color.colorPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp))
        Spacer(modifier = Modifier.height(8.dp))

    }
}