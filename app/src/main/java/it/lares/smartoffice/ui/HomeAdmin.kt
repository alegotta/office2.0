package it.lares.smartoffice.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import it.lares.smartoffice.BluetoothScanService
import androidx.appcompat.app.AppCompatActivity
import it.lares.smartoffice.R

import kotlinx.android.synthetic.main.activity_device_select.*

class HomeAdmin : AppCompatActivity() {

    private val TAG = "HomeAdmin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_select)
        setSupportActionBar(toolbar)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sendNewPoint()
    }

    fun sendNewPoint() {
        findViewById<Button>(R.id.btn_setNewPoint).setOnClickListener {
            val int = Intent(this, BluetoothScanService::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(int)
                Log.i("Home", "Started")
            } else {
                startService(int)
            }
        }
    }

}
