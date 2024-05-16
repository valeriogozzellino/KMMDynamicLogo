package ui.components.scannerQr

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.aakira.napier.Napier
import org.koin.compose.koinInject
import domain.scannerQR.ScannerViewModel
import platform.CameraPreviewWithQRCodeScanner

@Composable
fun QrScannerScreen(
    navController: NavController,
    scannerViewModel: ScannerViewModel = koinInject()

) {
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
        }
    }
}