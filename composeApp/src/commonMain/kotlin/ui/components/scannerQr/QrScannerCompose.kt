package chaintech.qrkit.demo.ui

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import qrscanner.QrScanner
import ui.logic.scannerQR.ScannerViewModel

@Composable
fun QrScannerCompose(
    navController: NavController,
    scannerViewModel: ScannerViewModel = koinInject()
) {
    Napier.d("TEST : sono nello scanner ")

    var qrCodeURL by remember { mutableStateOf("") }
    var startBarCodeScan by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
        Napier.d("TEST :------ 2 -------- ")
        Column(
            modifier = Modifier
                .background(color = Color.White)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (startBarCodeScan) {
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

                        /*--- function of library used to read QR ----*/
                        ScanQr()

                    }
                }
            } else {
                /*--- if qr code is scanned or is the first time in the app --- */
                Napier.d("TEST :------ 3 -------- ")
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    /*--- click to scan again ---*/
                    Button(
                        onClick = {
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

@Composable
fun ScanQr() {
    var flashlightOn by remember { mutableStateOf(false) }
    var launchGallery by remember { mutableStateOf(value = false) }
    var qrCodeURL by remember { mutableStateOf("") }

    QrScanner(
        modifier = Modifier
            .clipToBounds()
            .clip(shape = RoundedCornerShape(size = 14.dp)),
        flashlightOn = flashlightOn,
        launchGallery = launchGallery,
        onCompletion = {
            //Napier.d("TEST : STampo il contenuto del QR: ${scannerViewModel.base64Decoded(it)}")
            qrCodeURL = it
            scannerViewModel.analyseKey(it)
            //startBarCodeScan = true
        },
        onGalleryCallBackHandler = {
            launchGallery = it
        },
        onFailure = {
            Napier.d("TEST : ERROR scan QR code : $it")
        }
    )
}
