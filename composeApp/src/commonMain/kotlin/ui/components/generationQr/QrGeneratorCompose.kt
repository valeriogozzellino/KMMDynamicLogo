package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.components.generationQr.QRCodeViewer
import androidx.navigation.NavController
import org.koin.compose.koinInject
import ui.logic.generationQR.GenerateViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import io.github.aakira.napier.Napier
import ui.logic.generationQR.KeyManagerGeneration

/**
 * used buisness login to implements a qr code, used libs to generated qr code e defined Bitimage
 * */

@Composable
fun QrGeneratorCompose(
    navController: NavController,
    keyManagerGeneration: KeyManagerGeneration = koinInject(),
) {


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 22.dp)
            .verticalScroll(rememberScrollState())
    ) {

        QRCodeViewer()

        Button(
            onClick = {
                keyManagerGeneration.checkValid()
            },
            colors = ButtonDefaults.buttonColors(contentColor = Color(0xFF007AFF)),
            modifier = Modifier.padding(bottom = 20.dp)
        ) {
            Text(
                text = "check is Valid",
                modifier = Modifier.background(Color.Transparent)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}




