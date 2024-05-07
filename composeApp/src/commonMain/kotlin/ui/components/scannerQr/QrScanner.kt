package ui.components.scannerQr

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.koin.compose.koinInject
import ui.logic.scannerQR.ScannerViewModel

@Composable
fun QrScanner(
    navController: NavController,
    scannerViewModel: ScannerViewModel = koinInject()
) {

}