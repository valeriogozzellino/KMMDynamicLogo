package ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ui.components.scannerQr.QrScannerCompose
import io.github.aakira.napier.Napier
import ui.QrGeneratorCompose
import ui.screen.Home
import ui.screen.QrScreen

@Composable
fun AppNavigation () {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "screen") {
        composable("screen"){
            QrScreen(navController)
        }
        composable("scanner") {
            QrScannerCompose(navController)
        }
        composable("generator") {
            QrGeneratorCompose(navController)
        }
        composable("home"){
            Home(navController)
        }
    }
}