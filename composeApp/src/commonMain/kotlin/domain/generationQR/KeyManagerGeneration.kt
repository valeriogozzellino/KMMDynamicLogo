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
     * */
    fun generateKey(qrContent: String) {
        val timeSource = TimeSource.Monotonic
        key = QrKey(qrContent, timeSource.markNow())
        generateChunks()

        _startGeneration.value = true
    }

    /**
     * split key into chunks with the same length
     * */
    private fun generateChunks() {
        key?.let {
            val newChunks = splitString(it.key, (1..3).random())
            listChucks.addAll(newChunks)
        }
    }


    /**
     * add symbols between data
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
     * */
    private fun base64Encoded(input: String): String = input.encodeBase64()

    /**
     * generate random alphanumeric String using Kotlin library, internet source code
     * */
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private val randomlength = (50..70).random()

    private fun randomStringByKotlinRandom() =
        (0..randomlength).map { Random.nextInt(0, charPool.size).let { charPool[it] } }
            .joinToString("")


    /**
     * check if the key is still valid
     * */
    fun checkValid() {
        Napier.d("TEST : KEY IS VALID?--> ${key?.isStillValid()}")
    }

    /**
     * start validation time of the key, it's called in the moment
     * where the last chunk of the key is seen
     * */
    fun startValidationTime(element: QrData) {
        key?.let {
            it.generatedAt = timeSource.markNow()
        }
    }
}