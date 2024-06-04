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

fun commonModule() = module {
    singleOf(::GenerateViewModel)
    singleOf(::ScannerViewModel)
    singleOf(::KeyManagerGeneration)
    singleOf(::KeyManagerScanner)
    Napier.base(DebugAntilog())
}

expect fun platformModule(): Module


