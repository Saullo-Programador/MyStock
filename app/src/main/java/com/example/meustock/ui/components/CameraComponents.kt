package com.example.meustock.ui.components

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.rememberAsyncImagePainter
import com.example.meustock.R
import java.io.File



@Composable
fun CameraScreen(
    onBackClick: () -> Unit = {},
    onPhotoTaken: (String) -> Unit
){
    CameraContent(
        onBackClick = onBackClick,
        onPhotoTaken = onPhotoTaken
    )
}

@Composable
private fun CameraContent(
    onBackClick: () -> Unit = {},
    onPhotoTaken: (String) -> Unit
){

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                LifecycleCameraController.IMAGE_CAPTURE or LifecycleCameraController.VIDEO_CAPTURE
            )
        }
    }
    var tempUri by remember { mutableStateOf<String?>(null) }

    if (tempUri != null){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(tempUri),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .height(250.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { tempUri = null },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Red)
                        .padding(5.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_close),
                        contentDescription = "Fechar",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
                IconButton(
                    onClick = {
                        onPhotoTaken(tempUri!!)
                        onBackClick()
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Green)
                        .padding(5.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_check),
                        contentDescription = "Confirmar",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(
                    title = "Câmera",
                    onNavigationIconClick = { onBackClick() }
                )
            },
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .height(130.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        modifier = Modifier
                            .size(70.dp)
                            .border(3.dp, Color.White, CircleShape),
                        onClick = {
                            val photoFile = File.createTempFile("product_", ".jpg", context.cacheDir)
                            val outputOptions =
                                ImageCapture.OutputFileOptions.Builder(photoFile).build()

                            cameraController.takePicture(
                                outputOptions,
                                ContextCompat.getMainExecutor(context),
                                object : ImageCapture.OnImageSavedCallback {
                                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                        tempUri = photoFile.toURI().toString()
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        Toast.makeText(
                                            context,
                                            "Erro ao capturar foto",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )

                        }
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.icon_button_camera),
                            contentDescription = "Tirar Foto",
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                // Camera Preview
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .padding(paddingValues),
                    factory = { context ->
                        PreviewView(context).apply {
                            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                            setBackgroundColor(android.graphics.Color.BLACK)
                            scaleType = PreviewView.ScaleType.FILL_CENTER
                        }.also { previewView ->
                            previewView.controller = cameraController
                            cameraController.bindToLifecycle(lifecycleOwner)
                        }
                    }
                )

                // Moldura (retângulo) no centro da tela
                // Máscara preta com recorte no meio
                CameraMaskOverlay()
            }
        }
    }
}

@Composable
fun CameraMaskOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)) // fundo escuro
            .drawWithContent {
                // Moldura central
                val frameWidth = size.width
                val frameHeight = 250.dp.toPx()

                val left = 0f
                val right = frameWidth
                val top = (size.height - frameHeight) / 2

                val cornerRadius = 15.dp.toPx()

                // Desenha tudo normal (preto)
                drawContent()

                // "Recorta" o retângulo transparente no centro
                drawRoundRect(
                    color = Color.Transparent,
                    topLeft = Offset(left, top),
                    size = Size(right - left, frameHeight),
                    blendMode = BlendMode.Clear,
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                )
            }
    )
}
