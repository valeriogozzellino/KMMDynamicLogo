package platform.utils

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import io.github.aakira.napier.Napier

class QRCodeAnalyzer(private val onQRCodeDetected: (String) -> Unit) : ImageAnalysis.Analyzer {
    private val qrCodeReader = MultiFormatReader().apply {
        val hints = mapOf<DecodeHintType, Any>(
            DecodeHintType.POSSIBLE_FORMATS to listOf(
            BarcodeFormat.QR_CODE))
        setHints(hints)
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        if (imageProxy.format == ImageFormat.YUV_420_888) {
            val byteBuffer = imageProxy.planes[0].buffer
            val imageData = ByteArray(byteBuffer.capacity())
            byteBuffer.get(imageData)
            val source = PlanarYUVLuminanceSource(
                imageData, imageProxy.width, imageProxy.height, 0, 0, imageProxy.width, imageProxy.height, false
            )
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            try {
                val result = qrCodeReader.decodeWithState(binaryBitmap)
                Napier.d("TEST : stampo test QR Detected: ${result.text}")
                //onQRCodeDetected(result.text)
            } catch (e: NotFoundException) {
                // QR Code not found
            } finally {
                imageProxy.close()
            }
        }
    }
}

