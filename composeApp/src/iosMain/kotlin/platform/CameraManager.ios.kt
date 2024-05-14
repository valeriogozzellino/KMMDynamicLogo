package platform

import androidx.compose.runtime.Composable
import platform.AVFoundation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.aakira.napier.Napier
import platform.utils.QrCodeAnalyzer

private sealed interface CameraAccess {
    data object Undefined : CameraAccess
    data object Denied : CameraAccess
    data object Authorized : CameraAccess
}

private val deviceTypes = listOf(
    AVCaptureDeviceTypeBuiltInWideAngleCamera,
    AVCaptureDeviceTypeBuiltInDualWideCamera,
    AVCaptureDeviceTypeBuiltInDualCamera,
    AVCaptureDeviceTypeBuiltInUltraWideCamera,
    AVCaptureDeviceTypeBuiltInDuoCamera
)


@Composable
actual fun CameraPreviewWithQRCodeScanner(onQRCodeDetected: (String) -> Unit) {
    val modifier = Modifier
    val flashlightOn = false
    var cameraAccess: CameraAccess by remember { mutableStateOf(CameraAccess.Undefined) }

    LaunchedEffect(Unit) {
        when (AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)) {
            AVAuthorizationStatusAuthorized -> {
                cameraAccess = CameraAccess.Authorized
            }

            AVAuthorizationStatusDenied, AVAuthorizationStatusRestricted -> {
                cameraAccess = CameraAccess.Denied
            }

            AVAuthorizationStatusNotDetermined -> {
                AVCaptureDevice.requestAccessForMediaType(
                    mediaType = AVMediaTypeVideo
                ) { success ->
                    cameraAccess = if (success) CameraAccess.Authorized else CameraAccess.Denied
                }
            }
        }

    }
    Box(
        modifier.fillMaxSize().background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        when (cameraAccess) {
            CameraAccess.Undefined -> {
                // Waiting for the user to accept permission
            }

            CameraAccess.Denied -> {
                Text("Camera access denied", color = Color.White)
            }

            CameraAccess.Authorized -> {
                Napier.d("TEST : accesso consentito")
                AuthorizedCamera(flashlightOn = flashlightOn, onQRCodeDetected)

            }
        }
    }
    Napier.d("TEST : entrato in camera Preview")
}

@Composable
private fun BoxScope.AuthorizedCamera(flashlightOn: Boolean, onQRCodeDetected: (String) -> Unit) {
    val camera: AVCaptureDevice? = remember {
        AVCaptureDeviceDiscoverySession.discoverySessionWithDeviceTypes(
            deviceTypes = deviceTypes,
            mediaType = AVMediaTypeVideo,
            position = AVCaptureDevicePositionBack,
        ).devices.firstOrNull() as? AVCaptureDevice
    }
    Napier.d("TEST : autorizzazione camera")
    Napier.d("TEST : camera is NULL ? ${camera==null}")
    if (camera != null) {
        QrCodeAnalyzer(camera, onQRCodeDetected)
    }
}


