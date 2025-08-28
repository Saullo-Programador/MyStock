package com.example.meustock.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.meustock.R
import kotlinx.coroutines.selects.select

@Composable
fun ViewReact(
    type: String,
) {
    when(type){
        "Delete" -> {
            ImgLottie(img = R.raw.lottie_delete)
        }
        "Success" -> {
            ImgLottie(img = R.raw.lottie_success_check)
        }
        "Error" -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "Erro ao carregar dados" ,
                    color = Color.Red,
                    fontSize = 30.sp
                )
            }
        }
        "Loading" -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }
        else -> {
            ImgLottie(
                img = R.raw.codingslide
            )
        }

    }


}

@Composable
fun ImgLottie(
    img: Int,
    modifier: Modifier = Modifier,
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        contentAlignment = Alignment.Center
    ){
        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(img) )
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = modifier.fillMaxWidth()
        )
    }
}