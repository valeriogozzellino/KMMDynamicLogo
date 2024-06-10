package domain.generationQR

import androidx.compose.ui.graphics.ImageBitmap
import io.github.aakira.napier.Napier
import qrgenerator.generateQrCode
import data.QrData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class GenerateViewModel(private val keyManagerGeneration: KeyManagerGeneration) {


    /**
     * list for qr generator, every obj ha QrData Structure
     * */
    private val listInputQr =
        mutableListOf<QrData>() //contains all QrKey in order, need to make this random?
    private val _qrCodes = MutableStateFlow<ImageBitmap?>(null) //is the qr seen in the view
    private val listQrImg = mutableListOf<ImageBitmap>()
    private val timeSource = TimeSource.Monotonic
    private var startTimeGeneration = timeSource.markNow()
    private val durationLimit = 25.seconds
    private val mutex = Mutex()
    private var startVisualization = false

    /**
     * immutable variables that cannot be modify from external components
     * */
    val qrCodes = _qrCodes.asStateFlow()

    /**
     * generation of qr Code
     * @param
     * @return
     * */
    suspend fun generateQrCode() {

        val mark1 = timeSource.markNow()
        var tmpQrData: ImageBitmap? = null
        coroutineScope {
            launch(Dispatchers.Default) {
                val mark3 = timeSource.markNow()
                /*------- start generation time -------*/
                startTimeGeneration = timeSource.markNow()
                for (element in listInputQr) {
                    generateQrCode(
                        keyManagerGeneration.generateCriptedData(element),
                        onSuccess = { _, qrCode ->
                            tmpQrData = qrCode
                        },
                        onFailure = {
                            Napier.d("TEST : FAILS GENERATION QR")
                        }
                    )
                    val mark2 =
                        timeSource.markNow() //tempo impirgato da inizio codice a generazione
                    Napier.d("TEST : TIME GENERATION ONE  == (${mark2 - mark1})")

                    /*------ add to a list of qr code ------*/
                    generateQrCodelist(tmpQrData)

                    /*------- START TIME VALID--------*/
                    keyManagerGeneration.startValidationTime(element)

                }
                val mark4 = timeSource.markNow()
                Napier.d("TEST : TIME GENERATION LIST OF ${listInputQr.size} QRCODE  == (${mark4 - mark3})")
                Napier.d(" TEST : -----TERMINATA LA GENERAZIONE ----------")

            }
        }
    }

    /**
     * Error forse non aggiunge l'ultimo elemento
     * creating a list of lists using a temporary list based on step and total
     * property of the key generated
     * */
    private suspend fun generateQrCodelist(tmpQrData: ImageBitmap?) {
        tmpQrData?.let {
            mutex.withLock {
                listQrImg.add(it)
            }
        }
        startVisualization = listQrImg.size > 2
    }


    /**
     * iterate over a list of list of QRCodeImg randomly
     * @param
     * @return
     * */
    suspend fun generateVisualizationUI() {
        coroutineScope {
            launch(Dispatchers.Default) {
                Napier.d("TEST :numero di QR CREATI : ${listInputQr.size - 1}")
                val mark1 = timeSource.markNow() //start tempo di visualizzazione

                while (true) {
                    Napier.d("TEST : sono nel while ")

                    if (startVisualization) {
                        listQrImg.size
                        for (index in listInputQr.indices) {
                            if (startTimeGeneration.elapsedNow() >= durationLimit) {
                                Napier.d("TEST : --- TEMPO VISUALIZZAZIONE TERMINATO ----")
                                return@launch
                            }
                            mutex.withLock {
                                _qrCodes.value = listQrImg[index]
                            }
                            delay(600)
                            val mark2 = timeSource.markNow()
                            Napier.d("TEST : NUOVO QRCODE VISUALIZZATO ($index) : (${mark2 - mark1})")
                        }
                    }
                    delay(300)
                }
                val mark3 = timeSource.markNow()
                Napier.d("TEST : TEMPO TOTALE DI VISUALIZZAZIONE  : (${mark3 - mark1})")
            }
        }

    }

    /**
     * generate list of QRData from chunk
     * @param listChucks: list of  key's segments
     * @return
     * */
    fun generateListInputQr(listChucks: MutableList<String>) {
        for (i in listChucks.indices) {
            listInputQr.add(QrData(i, listChucks.lastIndex, listChucks[i], i))
        }
    }
}