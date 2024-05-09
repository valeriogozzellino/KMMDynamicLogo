package platform.utils

import androidx.compose.ui.graphics.ImageBitmap

expect class SharedImageOps {
    fun toByteArray(): ByteArray

    fun toImageBitmap(): ImageBitmap
}