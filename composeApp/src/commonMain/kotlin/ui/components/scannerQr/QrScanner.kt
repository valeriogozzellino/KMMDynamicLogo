package ui.components.scannerQr

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.github.aakira.napier.Napier
import org.koin.compose.koinInject
import domain.scannerQR.ScannerViewModel

@Composable
fun QrScanner(
    navController: NavController,
    scannerViewModel: ScannerViewModel = koinInject()
) {
    Napier.d("TEST : SONO IN SCANNER ")
}