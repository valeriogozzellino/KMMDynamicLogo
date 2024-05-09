package platform

import androidx.compose.runtime.Composable
import platform.utils.SharedImageOps

@Composable
expect fun rememberCameraManager(onResult: (SharedImageOps?) -> Unit): CameraManager


expect class CameraManager(
    onLaunch: () -> Unit
) {
    fun launch()
}