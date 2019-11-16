package it.lares.smartoffice.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import it.lares.smartoffice.http.HttpRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.security.MessageDigest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import it.lares.smartoffice.Application
import it.lares.smartoffice.app.Const.Companion.PREF_FILE
import it.lares.smartoffice.app.Const.Companion.SRV_URL
import it.lares.smartoffice.app.Const.Companion.USER_ADMIN
import it.lares.smartoffice.R
import it.lares.smartoffice.app.Const.Companion.PSW_STR
import it.lares.smartoffice.app.Const.Companion.USER_STR
import it.lares.smartoffice.app.Const.Companion.USR_TYPE
import it.lares.smartoffice.permission.RequestCallback
import it.lares.smartoffice.permission.RxPermissionRequest


class StartActivity : AppCompatActivity() {
    private lateinit var preferences : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        requestPermission()

        findViewById<Button>(R.id.btn_login).setOnClickListener {
            makeLogin()
        }

        preferences = getSharedPreferences(PREF_FILE, MODE_PRIVATE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.noti_channel)
            val descriptionText = getString(R.string.noti_channel)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("noti_channel", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

    }

    fun makeLogin() {
        val username = findViewById<EditText>(R.id.username).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()

        val hashPsw = hashString("SHA-256", password)
        val fake = false
        if (fake)
            switchScreen(username, hashPsw, USER_ADMIN)
        else {
            if (Application.checkConn(this)) {

                val req = HttpRequest()
                req.run("$SRV_URL/api/user/$username", object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("StartAcvear", "" + e.printStackTrace())
                        Toast.makeText(this@StartActivity, "Cannot login!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.use {
                            if (!response.isSuccessful) throw IOException("Unexpected code $response")
                            val resp: String

                            if (response.body != null) {
                                resp = response.body!!.string()

                                val json = JSONObject(resp)


                                if (json.getString("password") == hashPsw.toLowerCase()) {
                                    switchScreen(username, hashPsw, json.getInt("tipoUtente"))

                                } else {
                                    //Toast.makeText(StartActivity, "Wrong password!", Toast.LENGTH_SHORT).show()
                                    Log.e("StartAcvear", "Wrong password")
                                }
                            }
                        }
                    }
                })
            } else {
                Toast.makeText(this, "Error: missing network connection!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun switchScreen(username: String, hashPsw: String, userType: Int) {
        val preferencesEditor = preferences.edit()
        preferencesEditor.putString(USER_STR, username)
        preferencesEditor.putString(PSW_STR, hashPsw)
        preferencesEditor.putInt(USR_TYPE, userType)
        preferencesEditor.apply()

        var int = if (userType==USER_ADMIN) {
            Intent(this@StartActivity, HomeAdmin::class.java)
        } else {
            Intent(this@StartActivity, HomeNormal::class.java)
        }

        startActivity(int)
    }

    private fun hashString(type: String, input: String): String {
        val bytes = MessageDigest
                .getInstance(type)
                .digest(input.toByteArray())
        return printHexBinary(bytes).toUpperCase()
    }

    fun printHexBinary(data: ByteArray): String {
        val HEX_CHARS = "0123456789ABCDEF".toCharArray()

        val r = StringBuilder(data.size * 2)
        data.forEach { b ->
            val i = b.toInt()
            r.append(HEX_CHARS[i shr 4 and 0xF])
            r.append(HEX_CHARS[i and 0xF])
        }
        return r.toString()
    }

    private fun requestPermission() {
        val requestPermission = RxPermissionRequest()
        requestPermission.request(this, object : RequestCallback {
            override fun onRequestPermissionSuccess() {

            }

            override fun onRequestPermissionFailure() {
                Toast.makeText(this@StartActivity, getString(R.string.no_location_permission), Toast.LENGTH_SHORT)
            }

        }, android.Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1001 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }
}
