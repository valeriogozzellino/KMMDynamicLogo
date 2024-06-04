package domain.scannerQR

import io.github.aakira.napier.Napier
import io.ktor.util.decodeBase64String
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.utils.terminateScanProcess

class KeyManagerScanner() {

    private var totalKey = 0
    private var arrayChunk = mutableListOf<String>()
    private val _endScanner = MutableStateFlow(false)
    var endScanner = _endScanner.asStateFlow()
    private var key = ""

    /**
     * recompose KEY from qr code*/
    fun composeKey(qrContent: String) {

        val qrData = getContent(base64Decoded(qrContent))

        Napier.d("TEST :qr data ottenuto: == $qrData")
        Napier.d("TEST :qr content decodificato: == ${base64Decoded(qrContent)}")

        if (totalKey == 0) {
            totalKey = getTotalStep(base64Decoded(qrContent).split("::"))
        }

        val actualStep = getStep(base64Decoded(qrContent).split("::"))

        insertSegment(actualStep, qrData)

        if (totalKey == arrayChunk.size - 1) {
            composeSegment()
        }
        Napier.d("TEST: in KEY MANAGER -->  il contenuto del QR è : $qrData")

    }

    /**
     * insert chunk in the correct step
     * */
    private fun insertSegment(actualStep: Int, qrData: String) {
        if (arrayChunk.size == actualStep) { //l'elemento da inserire è quello corretto
            arrayChunk.add(qrData)
            Napier.d("TEST : ADD qr-content ---> step CORRETTO | step == $actualStep | size array == ${arrayChunk.size} ")
        } else if (arrayChunk.size < actualStep) { //l'elemento da inseire è di uno step successivo
            arrayChunk.add(" ")
            arrayChunk.add(qrData)
            Napier.d("TEST :  ADD qr-content ---> step maggiore aggiungo vuoto | step == $actualStep | size array == ${arrayChunk.size} ")
        } else { // lo step è minore della size, allora lo inserisco nello step corretto
            if (arrayChunk[actualStep] != qrData) {
                arrayChunk[actualStep] = qrData
            }
            Napier.d("TEST :  ADD qr-content ---> step minore sostituisco |  step == $actualStep | size array == ${arrayChunk.size}  ")
        }

    }


    /**
     * ead array of chunks and unified them in a string string
     * */
    private fun composeSegment() {
        Napier.d("TEST: ------ elementi in arrayCHunk: ${arrayChunk.size}")
        for (element in arrayChunk) {
            key += element
            Napier.d("TEST : ------ Stampo key == $element ")
        }
        Napier.d("TEST : ------ Stampo key == ${key} ")
        terminateScanProcess()
        _endScanner.value = true

    }


    /**
     * convert base 64 string to string
     * */
    private fun base64Decoded(input: String): String = input.decodeBase64String()

    /**
     * decode qr content
     * */
    private fun getContent(s: String): String {

        val parti = s.split("::")

        if (parti.size > 1) {

            return parti[1]

        } else {

            return ""

        }
    }

    /**
     * takes the step of chuck
     * */
    private fun getTotalStep(parti: List<String>): Int {
        val step = parti[0].split("__")
        Napier.d("TEST : sono dentro total KEY == ${step[1]}")
        return step[1].toInt()
    }

    /**
     * takes step of the chunk
     * */
    private fun getStep(parti: List<String>): Int {
        val step = parti[0].split("__")
        return step[0].toInt()
    }

}
