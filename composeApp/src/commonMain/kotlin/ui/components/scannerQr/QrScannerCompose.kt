package ui.components.scannerQr

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.aakira.napier.Napier
import org.koin.compose.koinInject
import domain.scannerQR.ScannerViewModel
import kmpImagePicker.AlertMessageDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.CameraPreviewWithQRCodeScanner
import platform.PermissionCallback
import platform.createPermissionsManager
import platform.models.PermissionStatus
import platform.models.PermissionType

@Composable
fun QrScannerCompose(
    navController: NavController,
    scannerViewModel: ScannerViewModel = koinInject()
) {
    Napier.d("TEST : Son nello scanner ---- 1 ")

    var qrCodeURL by remember { mutableStateOf("") }
    var startBarCodeScan by remember { mutableStateOf(false) }
    var launchCamera by remember { mutableStateOf(false) }
    val startScan by remember { mutableStateOf(0) }
    var permissionRationalDialog by remember { mutableStateOf(value = false) }
    var launchSetting by remember { mutableStateOf(value = false) }

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

    /*val cameraManager = rememberCameraManager{
        coroutineScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                it?.toImageBitmap()
            }
            imageBitmap = bitmap
        }
    }
*/
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (startBarCodeScan) {
                Napier.d("TEST :------- DENTRO IF -----")
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



                        if (launchCamera) {
                            CameraPreviewWithQRCodeScanner { Napier.d("STAMPO QR: $it") }
                           /* if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
                                cameraManager.launch()
                            } else {
                                permissionsManager.askPermission(PermissionType.CAMERA)
                            }*/
                            //launchCamera = false
                        }


                    }
                }
            } else {
                Napier.d("TEST :------- DENTRO ELSE -----")
                /*--- if qr code is scanned or is the first time in the app --- */
                if (startScan == 1) {
                    Napier.d("TEST :------- DENTRO ELSE--> IF -----")
                    startBarCodeScan = true
                    launchCamera = true
                } else {
                    Napier.d("TEST :------- DENTRO ELSE--> ELSE -----")
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        /*--- click to scan again ---*/
                        Button(
                            onClick = {
                                launchCamera = true
                                startBarCodeScan = true
                                qrCodeURL = ""
                            },
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

