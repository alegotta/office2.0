package it.lares.smartoffice.app

import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import android.app.*


class BeaconApplication : Application() {

    private var refWatcher: RefWatcher? = null

    override fun onCreate() {
        super.onCreate()
        refWatcher = setupLeakCanary()

    }

    private fun setupLeakCanary(): RefWatcher {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED
        }
        return LeakCanary.install(this)
    }

    companion object {

        val instance = BeaconApplication()
    }

}