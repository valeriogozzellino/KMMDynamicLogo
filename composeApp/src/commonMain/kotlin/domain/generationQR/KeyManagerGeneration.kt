package domain.generationQR

import QrKey
import data.QrData
import io.github.aakira.napier.Napier
import io.ktor.util.encodeBase64
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random
import kotlin.time.TimeSource

class KeyManagerGeneration() {

    private val _startGeneration = MutableStateFlow(false) //start generation of qrCode
    val listChucks = mutableListOf<String>()
    var key: QrKey? = null
    private val timeSource = TimeSource.Monotonic
    var startGeneration = _startGeneration.asStateFlow()

    /**
     * generation of the key and initialize the list of qr code with chunk
     * @param qrcontent: key splitted
     * @param timeValid:  duration where the kwy is valid
     * */
    fun generateKey(qrContent: String, timeValid: Int) {
        val timeSource = TimeSource.Monotonic
        key = QrKey(qrContent, timeSource.markNow(), timeValid)

        generateChunks()
        _startGeneration.value = true
    }

    /**
     * split key into chunks with the same length
     * @param
     * @return
     * */
    private fun generateChunks() {
        key?.let {
            val newChunks = splitString(it.key, (5..8).random())
            listChucks.addAll(newChunks)
        }
        Napier.d("TEST : NUMERO CHUNCKS GENERATI: ${listChucks.size-1}")
    }


    /**
     * add symbols between data
     * @param input:  QrData To be encripted
     * @return base64 ecripted data
     * */
    fun generateCriptedData(input: QrData): String {
        val mergedData: String =
            input.step.toString() + "__" + input.total.toString() + "::" + input.content
        return base64Encoded(mergedData)
    }

    /**
     * split key into chucks of equal length if possible
     * */
    private fun splitString(originalString: String, chunkSize: Int): List<String> {
        val chunks = mutableListOf<String>()
        var startIndex = 0

        while (startIndex < originalString.length) {
            val endIndex = startIndex + chunkSize
            if (endIndex <= originalString.length) {
                chunks.add(originalString.substring(startIndex, endIndex))
            } else {
                chunks.add(originalString.substring(startIndex))
            }
            startIndex = endIndex
        }

        return chunks
    }

    /**
     * decode String to Base64
     * @param
     * @return
     * */
    private fun base64Encoded(input: String): String = input.encodeBase64()

    /**
     * check if the key is still valid
     * @return
     *
     * */
    fun checkValid() {
        Napier.d("TEST : KEY IS VALID?--> ${key?.isStillValid()}")
    }

    /**
     * start validation time of the key, it's called in the moment
     * where the last chunk of the key is seen
     * @param
     * @retun
     * */
    fun startValidationTime(element: QrData) {
        key?.let {
            it.generatedAt = timeSource.markNow()
        }
    }
}