package in.hmorix.client

import android.app.Application
import in.hmorix.client.data.local.SessionManager

class HMorixApp : Application() {
    lateinit var sessionManager: SessionManager
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        sessionManager = SessionManager(this)
    }

    companion object {
        lateinit var instance: HMorixApp
            private set
    }
}
