package it.lares.office20.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import it.lares.office20.BluetoothScanService
import kotlinx.android.synthetic.main.activity_home.*


class HomeNormal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(it.lares.office20.R.layout.activity_home)
        setSupportActionBar(toolbar)

        val int = Intent(this, BluetoothScanService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(int)
            Log.i("Home", "Started")
        } else {
            startService(int)
        }
    }
}
