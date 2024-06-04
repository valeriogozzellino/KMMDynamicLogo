package ui.components.scannerQr

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import domain.scannerQR.KeyManagerScanner
import io.github.aakira.napier.Napier
import org.koin.compose.koinInject
import domain.scannerQR.ScannerViewModel
import platform.CameraPreviewWithQRCodeScanner

@Composable
fun QrScannerScreen(
    navController: NavController,
    scannerViewModel: ScannerViewModel = koinInject(),
    keyManagerScanner: KeyManagerScanner = koinInject()
) {
    val scannerEnd = keyManagerScanner.endScanner.collectAsState()
    LaunchedEffect(scannerEnd){
        if(scannerEnd.value){
            navController.navigate("keyscreen")
        }
    }
    val infiniteTransition = rememberInfiniteTransition()
    val offsetY = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 250f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )


    Column(
        modifier = Modifier
            .background(color = Color.Black)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .clip(shape = RoundedCornerShape(size = 14.dp))
                .clipToBounds()
                .border(2.dp, Color.Gray, RoundedCornerShape(size = 14.dp)),
            contentAlignment = Alignment.Center
        ) {


            CameraPreviewWithQRCodeScanner { Napier.d("STAMPO QR: $it") }
            // Aggiungiamo una linea che si muove su e giù
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(y = offsetY.value.dp)
                    .width(250.dp)
                    .height(2.dp)
                    .background(Color.Red)
            )
        }
        Button(
            onClick = {
                navController.navigate("home")
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF4A90E2),
                contentColor = Color.White
            ),
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(
                text = "go Home",
                modifier = Modifier.background(Color.Transparent)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                fontSize = 16.sp,
                color = Color.White
            )
        }
        Button(
            onClick = {
               navController.navigate("keyscreen")
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF4A90E2),
                contentColor = Color.White
            ),
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(
                text = "check key",
                modifier = Modifier.background(Color.Transparent)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}


