package platform

import androidx.compose.runtime.Composable

@Composable
expect fun CameraPreviewWithQRCodeScanner(onQRCodeDetected: (String) -> Unit)

