package com.example.meustock.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.meustock.R

@Composable
fun ViewReact(
    type: String,
    onFinished: (() -> Unit)? = null,
) {
    when(type){
        "Delete" -> {
            ImgLottie(img = R.raw.lottie_delete, onFinished = onFinished)
        }
        "Success" -> {
            ImgLottie(img = R.raw.lottie_success_check, onFinished = onFinished)
        }
        "Error" -> {
            ImgLottie(img = R.raw.lottie_error, onFinished = onFinished)
        }
        "Loading" -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }

    }


}

@Composable
fun ImgLottie(
    img: Int,
    onFinished: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        contentAlignment = Alignment.Center
    ){
        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(img) )
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = 1,
        )

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = modifier
                .height(150.dp)
                .width(150.dp),
        )

        if(progress == 1f){
            onFinished?.invoke()
        }

    }
}