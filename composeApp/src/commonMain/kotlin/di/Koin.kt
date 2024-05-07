package di

import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import ui.logic.generationQR.GenerateViewModel
import ui.logic.generationQR.KeyManagerGeneration
import ui.logic.scannerQR.ScannerViewModel

fun initKoin(
    appDeclaration: KoinAppDeclaration = {}
) = startKoin{
    appDeclaration()
    modules(commonModule())
}

fun KoinApplication.Companion.start(): KoinApplication = initKoin{}


val Koin.generateViewModel: GenerateViewModel
    get() = get()

val Koin.scannerViewModel: ScannerViewModel
    get() = get()

val Koin.keyManagerGeneration: KeyManagerGeneration
    get() = get()
