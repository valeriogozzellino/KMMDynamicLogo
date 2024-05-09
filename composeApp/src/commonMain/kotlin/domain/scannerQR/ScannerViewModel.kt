package domain.scannerQR

import getPlatform
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class ScannerViewModel(private val keyManagerScanner: KeyManagerScanner) {

    /**
     * immutable variables that cannot be modify from external components
     * */
    private val _qrCodesScan = MutableStateFlow<Boolean>(false) //is the qr seen in the view
    val qrCodesScan = _qrCodesScan.asStateFlow()


    /**
     * send data when it scan all qr in the list
     * */
    fun sendStringQR(qrCodeURL: String) {

        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    isLenient = true
                    prettyPrint = true
                    ignoreUnknownKeys = true
                })
            }
        }
        try {

            if (qrCodeURL != "") {
                runBlocking {

                    val url = when (getPlatform().name) {
                        "Android" -> "http://10.0.2.2:3001/test/test-qr/$qrCodeURL"
                        else -> "http://10.38.95.35:3001/test/test-qr/$qrCodeURL"
                    }
                    val responseData = client.get(url)
                    Napier.d(responseData.toString())
                }
            } else {
                //gestire il fatto che non ci sia un ID
            }

        } catch (e: Exception) {
            Napier.d("TEST:ERRORRREEEEEEEEEEEE NELLA CHIAMATA $e")
        } finally {
            client.close()
        }
    }

    suspend fun printKey(qrContent: String) {
       Napier.d ("TEST : il contenuto del Qr code è : $qrContent")
        //keyManagerScanner.composeKey(qrContent)
        _qrCodesScan.value = !_qrCodesScan.value
    }


}