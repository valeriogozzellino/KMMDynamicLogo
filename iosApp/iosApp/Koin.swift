import ComposeApp
import Foundation

private var _koin: Koin_coreKoin? = nil
var koin: Koin_coreKoin {
    return _koin!
}

func startKoin () {
    let koinApplication = DependencyInjectionKtKt.doInitKoinIOS()
    _koin = koinApplication.koin
}
