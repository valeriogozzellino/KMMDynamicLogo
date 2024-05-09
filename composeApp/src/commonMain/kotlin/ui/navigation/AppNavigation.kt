package ui.navigation

import QrScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ui.components.scannerQr.QrScannerCompose
import io.github.aakira.napier.Napier
import ui.QrGeneratorCompose

@Composable
fun AppNavigation () {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "screen") {
        composable("screen"){
            QrScreen(navController)
        }
        composable("scanner") {
            Napier.d("TEST: entro nello scanner")
            QrScannerCompose(navController)
        }
        composable("generator") {
            QrGeneratorCompose(navController)
        }

    }
}