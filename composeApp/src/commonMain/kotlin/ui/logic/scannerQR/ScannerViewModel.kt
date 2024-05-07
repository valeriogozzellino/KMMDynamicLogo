package ui.logic.scannerQR

import chaintech.qrkit.demo.ui.QrScannerCompose
import data.QrData
import getPlatform
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.decodeBase64String
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import qrscanner.scanImage

class ScannerViewModel() {

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

    /**
     * can convert base 64 string to string
     * */

    fun base64Decoded(input: String): String = input.decodeBase64String()


    fun analyseKey(qrContent: String) {
       Napier.d ("TEST : il contenuto del Qr code è : $qrContent")
    }


}