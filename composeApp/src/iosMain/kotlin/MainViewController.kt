import androidx.compose.ui.window.ComposeUIViewController
import di.commonModule
import di.platformModule
import org.koin.core.context.startKoin
import ui.navigation.AppNavigation
import ui.screen.App

fun MainViewController() = ComposeUIViewController { App() }

fun initKoin() {
    startKoin{
        modules(commonModule() + platformModule())
    }
}