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

class GenerateViewModel(private val keyManagerGeneration: KeyManagerGeneration) {

    /**
     * @TODO implements
     * - gestire in modo più efficiente i metodi private e pubblici
     * - gestire una sola key e non liste di key !!!!1
     * */

    /**
     * list for qr generator, every obj ha QrData Structure
     * */
    private val listInputQr = mutableListOf<QrData>() //contains all QrKey in order, need to make this random?
    private val _qrCodes = MutableStateFlow<ImageBitmap?>(null) //is the qr seen in the view
    private val listQrImg = mutableListOf<ImageBitmap>()

    /**
     * immutable variables that cannot be modify from external components
     * */
    val qrCodes = _qrCodes.asStateFlow()

    /**
     * generation of qr Code
     * */
    suspend fun generateQrCode() {

        var tmpQrData: ImageBitmap? = null
        coroutineScope {
            launch(Dispatchers.Default) {
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
                    delay(100)
                    _qrCodes.value = tmpQrData

                    /*------ add to a list of qr code ------*/
                    generateQrCodelist(tmpQrData)

                    /*------- START TIME VALID--------*/
                    keyManagerGeneration.startValidationTime(element)
                }
                generateRandomSequence()
                Napier.d( " TEST : -----TERMINATO IL CASO D'USO----------")

            }
        }
    }



    /**
     * Error forse non aggiunge l'ultimo elemento
     * creating a list of lists using a temporary list based on step and total
     * property of the key generated
     * */
    private fun generateQrCodelist(tmpQrData: ImageBitmap?) {
        if (tmpQrData != null) {
            listQrImg.add(tmpQrData)
        }
    }


    /**
     * iterate over a list of list of QRCodeImg randomly
     * */
    suspend fun generateRandomSequence() {
        coroutineScope {
            launch(Dispatchers.Default) {
                //val randomKey = (0..listQrKey.lastIndex).random()
                for(element in listQrImg){

                    Napier.d("TEST : SONO NEL FOR IN RANDOM SEQUENCE")
                    _qrCodes.value = element

                    delay(200)

                }
            }
        }

    }


    /**
     * @TODO Implements---> cercare di richiamare questa funzione per ogni qr alla volta quando effettivamente necessario --> computazione o^2
     * generate list of QRData from chunk
     * */
    fun generateListInputQr(listChucks: MutableList<String>){
        for (i in listChucks.indices) {
                listInputQr.add(QrData( i, listChucks.lastIndex, listChucks[i], i))
        }
    }


}