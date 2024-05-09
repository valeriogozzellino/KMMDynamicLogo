package domain.scannerQR

import io.github.aakira.napier.Napier
import io.ktor.util.decodeBase64String
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class KeyManagerScanner() {

    /**
     * recompose KEY from qr code*/
    suspend fun  composeKey(qrContent: String) {
        coroutineScope {
            launch(Dispatchers.Default) {
                val qrData = getContent( base64Decoded(qrContent))
                Napier.d("il contenuto del QR è : $qrData")
            }
        }
    }

    /**
     * can convert base 64 string to string
     * */

    private fun base64Decoded(input: String): String = input.decodeBase64String()

    private fun getContent (s: String): String {

        val parti = s.split("::")
        return if (parti.size > 1) parti[1] else ""
    }
}