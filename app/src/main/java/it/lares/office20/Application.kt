package it.lares.office20

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


class Application : Application() {
    private val mContext: Application? = null

    companion object {
        fun checkConn(context: Context) : Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            return activeNetwork?.isConnectedOrConnecting == true
        }
    }
}