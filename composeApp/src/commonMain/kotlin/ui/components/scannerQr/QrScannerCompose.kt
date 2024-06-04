package ui.components.scannerQr

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import domain.scannerQR.KeyManagerScanner
import io.github.aakira.napier.Napier
import org.koin.compose.koinInject
import domain.scannerQR.ScannerViewModel
import kmpImagePicker.AlertMessageDialog
import kotlinqrcodeproject.composeapp.generated.resources.Res
import kotlinqrcodeproject.composeapp.generated.resources.start_scanning
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import platform.*
import platform.models.*
import platform.utils.startScanProcess

@OptIn(ExperimentalResourceApi::class)
@Composable
fun QrScannerCompose(
    navController: NavController,
    scannerViewModel: ScannerViewModel = koinInject(),
    keyManagerScanner: KeyManagerScanner = koinInject()
) {
    Napier.d("TEST : Son nello scanner ---- 1 ")

    var qrCodeURL by remember { mutableStateOf("") }
    var startBarCodeScan by remember { mutableStateOf(false) }
    var launchCamera by remember { mutableStateOf(false) }
    val startScan by remember { mutableStateOf(0) }
    var permissionRationalDialog by remember { mutableStateOf(value = false) }
    var launchSetting by remember { mutableStateOf(value = false) }
    val endScanner = keyManagerScanner.endScanner.collectAsState()

    //se la scannerizzazione è terminata ed è validato il token --> navigo
    LaunchedEffect(endScanner) {
        if (endScanner.value) {
            startBarCodeScan = false
            //navController.navigate("home")
        }
    }


    val permissionsManager = createPermissionsManager(object : PermissionCallback {
        override fun onPermissionStatus(
            permissionType: PermissionType,
            status: PermissionStatus
        ) {
            when (status) {
                PermissionStatus.GRANTED -> {
                    when (permissionType) {
                        PermissionType.CAMERA -> launchCamera = true
                    }
                }

                else -> {
                    permissionRationalDialog = true
                }
            }
        }

    })

    if (launchSetting) {
        permissionsManager.launchSettings()
        launchSetting = false
    }

    if (permissionRationalDialog) {
        AlertMessageDialog(title = "Permission Required",
            message = "To set your profile picture, please grant this permission. You can manage permissions in your device settings.",
            positiveButtonText = "Settings",
            negativeButtonText = "Cancel",
            onPositiveClick = {
                permissionRationalDialog = false
                launchSetting = true

            },
            onNegativeClick = {
                permissionRationalDialog = false
            })

    }

    Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
        Column(
            modifier = Modifier
                .background(color = Color.White)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize(),
        ) {
            if (startBarCodeScan) {
                startScanProcess()
                QrScannerScreen(navController)

            } else {
                /*--- if qr code is scanned or is the first time in the app --- */
                if (startScan == 1) {
                    startBarCodeScan = true
                    launchCamera = true
                } else {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
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
                                IconButton(onClick = { navController.navigate("screen") }) {
                                    Icon(Icons.Filled.ArrowBack, contentDescription = "Menu")
                                }
                            },
                            backgroundColor = Color(0xFF4A90E2),
                            contentColor = Color.White,
                            elevation = 4.dp
                        )
                        Column(
                            modifier = Modifier.padding(top = 120.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(
                                    resource = Res.drawable.start_scanning
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(top = 20.dp)
                                    .width(160.dp)
                                    .height(200.dp)
                            )
                            /*--- click to scan again ---*/
                            Button(
                                onClick = {
                                    launchCamera = true
                                    startBarCodeScan = true
                                    qrCodeURL = ""
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xFF4A90E2),
                                    contentColor = Color.White
                                ),
                            ) {
                                Text(
                                    text = "Scan Qr",
                                    modifier = Modifier.background(Color.Transparent)
                                        .padding(horizontal = 12.dp, vertical = 12.dp),
                                    fontSize = 16.sp
                                )
                            }

                            Text(
                                text = qrCodeURL,
                                color = Color.Black,
                                modifier = Modifier.padding(top = 12.dp)
                            )
                        }
                    }
                }
            }
        }

        /*--- if qr code is open, i can close camera changing the value of mutableState -----*/
        if (startBarCodeScan) {
            Icon(
                imageVector = Icons.Filled.Close,
                "Close",
                modifier = Modifier
                    .padding(top = 12.dp, end = 12.dp)
                    .size(24.dp)
                    .clickable {
                        startBarCodeScan = false
                    }.align(Alignment.TopEnd),
                tint = Color.White
            )
        }
    }

}

