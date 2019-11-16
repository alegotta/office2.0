package it.lares.smartoffice.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import it.lares.smartoffice.BluetoothScanService
import com.google.android.material.snackbar.Snackbar
import it.lares.smartoffice.app.BeaconApplication
import kotlinx.android.synthetic.main.activity_home.*
import androidx.core.content.ContextCompat.getSystemService
import android.R
import android.annotation.TargetApi
import android.app.*
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ComponentActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class HomeNormal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(it.lares.smartoffice.R.layout.activity_home)
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
