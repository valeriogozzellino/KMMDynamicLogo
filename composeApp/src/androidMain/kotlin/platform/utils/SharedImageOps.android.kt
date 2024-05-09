package platform.utils

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

actual class SharedImageOps(private val bitmap: android.graphics.Bitmap) {
    actual fun toByteArray(): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        @Suppress("MagicNumber") bitmap.compress(
            android.graphics.Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream
        )
        return byteArrayOutputStream.toByteArray()
    }


    actual fun toImageBitmap(): ImageBitmap {
        val byteArray = toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap()
    }

}