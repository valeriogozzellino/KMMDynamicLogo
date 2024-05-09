package platform.kmp.image.picker

import android.app.Application
import di.initKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {}
    }
}