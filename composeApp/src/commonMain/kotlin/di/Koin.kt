package di

import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import domain.generationQR.GenerateViewModel
import domain.generationQR.KeyManagerGeneration
import domain.scannerQR.KeyManagerScanner
import domain.scannerQR.ScannerViewModel

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

val Koin.keyManagerScanner: KeyManagerScanner
    get() = get()
