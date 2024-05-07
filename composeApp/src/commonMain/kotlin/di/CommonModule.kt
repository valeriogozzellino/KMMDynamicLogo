package di

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ui.logic.generationQR.GenerateViewModel
import ui.logic.generationQR.KeyManagerGeneration
import ui.logic.scannerQR.ScannerViewModel

/*creo le dipendenze in questo file
 single serve per istanziare una sola entita di questa classe
 la prima volta che questa viene richiesta
 posso anche definire delle interfacce composte che vengono iniettate
 in una classe per avere una maggior efficenza  modularizzazione*/

fun commonModule() = module {
    singleOf(::GenerateViewModel)
    singleOf(::ScannerViewModel)
    singleOf(::KeyManagerGeneration)
    Napier.base(DebugAntilog())
}

expect fun platformModule(): Module


