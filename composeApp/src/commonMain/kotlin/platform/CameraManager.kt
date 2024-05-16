package platform

import androidx.compose.runtime.Composable

/*@Composable
expect fun rememberCameraManager(onQRCodeDetected: (String) -> Unit): CameraManager


expect class CameraManager(
    onLaunch: () -> Unit
) {
    fun launch()
}*/

@Composable
expect fun CameraPreviewWithQRCodeScanner(onQRCodeDetected: (String) -> Unit)
