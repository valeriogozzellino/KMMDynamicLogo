package platform.utils

import androidx.compose.runtime.Composable
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import platform.AVFoundation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitView
import io.github.aakira.napier.Napier
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ObjCAction
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import platform.CoreGraphics.CGRect
import platform.Foundation.NSNotification
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSSelectorFromString
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCATransactionDisableActions
import platform.UIKit.UIDevice
import platform.UIKit.UIDeviceOrientation
import platform.UIKit.UIDeviceOrientationDidChangeNotification
import platform.UIKit.UIView
import platform.darwin.*
import domain.scannerQR.KeyManagerScanner
import kotlin.concurrent.Volatile
import kotlin.time.*

@Volatile
private var isActive =  mutableStateOf(true)



/**
 * Analizer of qr code for ios, it creates a session of AVCapture
 * @param camera: AVCapture device camera
 * @param onQRCodeDetected: function returned the string of the qr Code
 * @return
 * */
@OptIn(ExperimentalForeignApi::class, DelicateCoroutinesApi::class, BetaInteropApi::class)
@Composable
fun QrCodeAnalyzer(
    camera: AVCaptureDevice,
    onQRCodeDetected: (String) -> Unit,
) {
    val timeSource = TimeSource.Monotonic
    val keyManager = KeyManagerScanner()
    val capturePhotoOutput = remember { AVCapturePhotoOutput() }
    var actualOrientation by remember {
        mutableStateOf(
            AVCaptureVideoOrientationPortrait
        )
    }

    val captureSession: AVCaptureSession = remember {
        AVCaptureSession().also { captureSession ->
            captureSession.sessionPreset = AVCaptureSessionPresetPhoto
            val captureDeviceInput: AVCaptureDeviceInput =
                AVCaptureDeviceInput.deviceInputWithDevice(device = camera, error = null)!!

            if (captureSession.canAddInput(captureDeviceInput)) {
                captureSession.addInput(captureDeviceInput)
            }

            if (captureSession.canAddOutput(capturePhotoOutput)) {
                captureSession.addOutput(capturePhotoOutput)
            }

            val metadataOutput = AVCaptureMetadataOutput()
            if (captureSession.canAddOutput(metadataOutput)) {
                captureSession.addOutput(metadataOutput)
                metadataOutput.setMetadataObjectsDelegate(
                    objectsDelegate = object : NSObject(),
                        AVCaptureMetadataOutputObjectsDelegateProtocol {
                        override fun captureOutput(
                            output: AVCaptureOutput,
                            didOutputMetadataObjects: List<*>,
                            fromConnection: AVCaptureConnection,
                        ) {
                            if (!isActive.value) {
                                return  // Termina l'analisi se la sessione non è più attiva
                            }
                            didOutputMetadataObjects.firstOrNull()?.let { metadataObject ->
                                /*------ MARKER -------*/
                                val mark1 = timeSource.markNow()
                                val readableObject =
                                    metadataObject as? AVMetadataMachineReadableCodeObject

                                val code = readableObject?.stringValue

                                if (!code.isNullOrEmpty()) {
                                    Napier.d("TEST : QR detected !!!! === $code \n")
                                    keyManager.composeKey(code) //chiamo la ricomposizione della key del key manager

                                    val mark2 = timeSource.markNow()
                                    Napier.d("TEST : TIME DETECTED QRCODE IOS  == (${mark2 - mark1})")
                                    onQRCodeDetected.invoke(code)
                                }
                            }
                        }
                    }, queue = dispatch_get_main_queue() //gestisce i thread maim
                )

                metadataOutput.metadataObjectTypes = listOf(
                    AVMetadataObjectTypeQRCode,
                    AVMetadataObjectTypeMicroQRCode,
                )
            }
        }
    }
    val cameraPreviewLayer = remember {
        AVCaptureVideoPreviewLayer(session = captureSession)
    }

    DisposableEffect(Unit) {
        class OrientationListener : NSObject() {
            @Suppress("UNUSED_PARAMETER")
            @ObjCAction
            fun orientationDidChange(arg: NSNotification) {
                val cameraConnection = cameraPreviewLayer.connection
                if (cameraConnection != null) {
                    actualOrientation = when (UIDevice.currentDevice.orientation) {
                        UIDeviceOrientation.UIDeviceOrientationPortrait ->
                            AVCaptureVideoOrientationPortrait

                        UIDeviceOrientation.UIDeviceOrientationLandscapeLeft ->
                            AVCaptureVideoOrientationLandscapeRight

                        UIDeviceOrientation.UIDeviceOrientationLandscapeRight ->
                            AVCaptureVideoOrientationLandscapeLeft

                        UIDeviceOrientation.UIDeviceOrientationPortraitUpsideDown ->
                            AVCaptureVideoOrientationPortrait

                        else -> cameraConnection.videoOrientation
                    }
                    cameraConnection.videoOrientation = actualOrientation
                }
                capturePhotoOutput.connectionWithMediaType(AVMediaTypeVideo)
                    ?.videoOrientation = actualOrientation
            }
        }

        val listener = OrientationListener()
        val notificationName = UIDeviceOrientationDidChangeNotification
        NSNotificationCenter.defaultCenter.addObserver(
            observer = listener,
            selector = NSSelectorFromString(
                OrientationListener::orientationDidChange.name + ":"
            ),
            name = notificationName,
            `object` = null
        )
        onDispose {
            NSNotificationCenter.defaultCenter.removeObserver(
                observer = listener,
                name = notificationName,
                `object` = null
            )
        }
    }
    UIKitView(
        modifier = Modifier.fillMaxSize(),
        background = Color.Black,
        factory = {
            val cameraContainer = UIView()
            cameraContainer.layer.addSublayer(cameraPreviewLayer)
            cameraPreviewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill
            GlobalScope.launch(Dispatchers.IO) {
                captureSession.startRunning()
            }
            cameraContainer
        },
        onResize = { view: UIView, rect: CValue<CGRect> ->
            CATransaction.begin()
            CATransaction.setValue(true, kCATransactionDisableActions)
            view.layer.setFrame(rect)
            cameraPreviewLayer.setFrame(rect)
            CATransaction.commit()
        },
    )
}



actual fun terminateScanProcess() {
    isActive.value = false
}

actual fun startScanProcess() {
    isActive.value = true
}