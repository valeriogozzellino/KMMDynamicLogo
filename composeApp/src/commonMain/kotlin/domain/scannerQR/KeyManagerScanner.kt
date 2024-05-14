package domain.scannerQR

import io.github.aakira.napier.Napier
import io.ktor.util.decodeBase64String
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class KeyManagerScanner() {

    var totalKey = 0
    var arrayChunk = arrayOf<String>()
    private val _endScanner = MutableStateFlow(false)
    var endScanner = _endScanner.asStateFlow()

    /**
     * recompose KEY from qr code*/
    fun composeKey(qrContent: String) {
        //check end scan
        //checkEndScan()
        //convert key and decompose
        val qrData = getContent(base64Decoded(qrContent))
        totalKey = getTotalStep(qrContent.split("::"))
        arrayChunk = Array(totalKey) { "" }  // Ogni elemento è una stringa vuota
        val actualStep = getStep(qrContent.split("::"))
        Napier.d("TEST: in KEY MANAGER -->  il contenuto del QR è : $qrData")

        arrayChunk[actualStep] = qrData
    }

    /**
     * step 1
     * convert base 64 string to string
     * */
    private fun base64Decoded(input: String): String = input.decodeBase64String()

    /**
     * step 2
     * */
    private fun getContent(s: String): String {

        val parti = s.split("::")

        if (parti.size > 1) {

            if (totalKey == 0) {
                getTotalStep(parti)
            }

            return parti[1]

        } else {

            return ""

        }
    }

    /**
     *
     * */
    private fun getTotalStep(parti: List<String>): Int {
        val step = parti[0].split("__")
        return step[1].toInt()
    }

    /**
     *
     * */
    private fun getStep(parti: List<String>): Int {
        val step = parti[0].split("__")
        return step[0].toInt()
    }


    /**
     * check if all QRCode were scanned*/
   /* private fun checkEndScan() {
        _endScanner.value = (arrayChunk.size - 1 == totalKey)
    }*/
}
