package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.components.generationQr.QRCodeViewer
import androidx.navigation.NavController
import org.koin.compose.koinInject
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import domain.generationQR.KeyManagerGeneration

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
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "QR Code Project",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            navigationIcon = {
                IconButton(onClick = {navController.navigate("screen") }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Menu")
                }
            },
            backgroundColor = Color(0xFF4A90E2),
            contentColor = Color.White,
            elevation = 4.dp
        )
        QRCodeViewer()

        Button(
            onClick = {
                keyManagerGeneration.checkValid()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF4A90E2),
                contentColor = Color.White
            ),
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




