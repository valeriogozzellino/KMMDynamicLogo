package ui.components.generationQr

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import io.github.aakira.napier.Napier
import org.koin.compose.koinInject
import ui.logic.generationQR.GenerateViewModel
import ui.logic.generationQR.KeyManagerGeneration


@Composable
fun QRCodeViewer(
    generateViewModel: GenerateViewModel = koinInject(),
    keyManagerGeneration: KeyManagerGeneration = koinInject()
) {

    val qrCodes = generateViewModel.qrCodes.collectAsState()
    val startQrGeneration = keyManagerGeneration.startGeneration.collectAsState()

    LaunchedEffect(Unit){
        keyManagerGeneration.generateKey("Valerio-Gozzellino-995974-08/04/02-Asti-Calamandrana-Studente-informatica")
    }

    LaunchedEffect(startQrGeneration) {
        if (keyManagerGeneration.listChucks.isNotEmpty()) {
            generateViewModel.generateListInputQr(keyManagerGeneration.listChucks)
            generateViewModel.generateQrCode()
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 22.dp),
    ) {
        if (qrCodes.value != null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color.White)
                    .border(BorderStroke(3.dp, Color.Black))
                    .size(250.dp)
            ) {
                Image(
                    bitmap = qrCodes.value!!,
                    contentScale = ContentScale.Fit,
                    contentDescription = "QR Code",
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            QRCodeShimmer()
        }

    }
}
