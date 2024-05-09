package di

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import domain.generationQR.GenerateViewModel
import domain.generationQR.KeyManagerGeneration
import domain.scannerQR.ScannerViewModel
import domain.scannerQR.KeyManagerScanner

/*creo le dipendenze in questo file
 single serve per istanziare una sola entita di questa classe
 la prima volta che questa viene richiesta
 posso anche definire delle interfacce composte che vengono iniettate
 in una classe per avere una maggior efficenza  modularizzazione*/

fun commonModule() = module {
    singleOf(::GenerateViewModel)
    singleOf(::ScannerViewModel)
    singleOf(::KeyManagerGeneration)
    singleOf(::KeyManagerScanner)
    Napier.base(DebugAntilog())
}

expect fun platformModule(): Module


expect fun startScanQr()
