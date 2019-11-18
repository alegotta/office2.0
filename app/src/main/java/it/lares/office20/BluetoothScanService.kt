package it.lares.office20

import android.R
import android.app.*
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.RemoteException
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import it.lares.office20.app.Const
import it.lares.office20.app.Const.Companion.SRV_URL
import it.lares.office20.app.Const.Companion.USER_BASIC
import it.lares.office20.app.Const.Companion.USER_STR
import it.lares.office20.app.Const.Companion.USR_TYPE
import it.lares.office20.http.HttpPost
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.altbeacon.beacon.*
import org.altbeacon.beacon.powersave.BackgroundPowerSaver
import org.json.JSONObject
import java.io.IOException
import java.util.*


/**
 * Service used to scan for bluetooth beacons
 */
class BluetoothScanService : IntentService("BluetoothScanService"), BeaconConsumer {

    private lateinit var beaconManager: BeaconManager
    private val TAG = "BluetoothScanService"
    private lateinit var preferences: SharedPreferences
    private var count = 0   //Counter for the insertPoint request
    private val region = Region("Range", null, null, null)

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "onStartCommand called")

        //Enable bluetooth
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!mBluetoothAdapter.isEnabled) {
            mBluetoothAdapter.enable()
        }

        val notification = getNotificationBuilder("1")
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_btn_speak_now)
                .setContentTitle("Scanning")
                .setContentText("Scanning")
                .setChannelId("1")
                .setPriority(Notification.PRIORITY_MAX)
                .build()

        startForeground(1, notification)
    }

    /**
     * The service was started: initialize the beacon scan
     */
    override fun onHandleIntent(p0: Intent?) {
        Log.i(TAG, "Started service")

        beaconManager = BeaconManager.getInstanceForApplication(this)
        //Filter for eddystone beacons only
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT))
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT))


        val backgroundPowerSaver = BackgroundPowerSaver(this)
        beaconManager.backgroundBetweenScanPeriod = 0
        beaconManager.backgroundScanPeriod = 1000   //Scan every second

        beaconManager.bind(this)

        preferences = getSharedPreferences(Const.PREF_FILE, AppCompatActivity.MODE_PRIVATE)

        Log.i(TAG, "Started bluetooth scan")
    }

    private fun getNotificationBuilder(channelId: String) : NotificationCompat.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(Activity.NOTIFICATION_SERVICE) as NotificationManager

            var nChannel: NotificationChannel? = nm.getNotificationChannel(channelId)

            if (nChannel == null) {
                nChannel = NotificationChannel(channelId, "Office 2.0", NotificationManager.IMPORTANCE_HIGH)
                nm.createNotificationChannel(nChannel)
            }
            NotificationCompat.Builder(this, channelId)
        } else {
            NotificationCompat.Builder(this)
        }
    }

    override fun onBeaconServiceConnect() {
        /**
         * Listen for bluetooth beacons.
         * It returns a JSON object with user id, timestamp of the scan, then various fields for every beacon
         */
        beaconManager.addRangeNotifier { beacons, _ ->
            Log.i(TAG, "Total number of beacons: " + beacons.size)

            if (beacons.size >= 3) {
                val data = mutableListOf<JSONObject>()
                //beacons.filter {
                    //Optional: filter for specific namespaces/instance IDs TODO: Remove this as it was used only for demo purposes
                beacons.filter {
                    it.id2 == Identifier.parse("0x000000000040") || it.id2 == Identifier.parse("0x000000000041")|| it.id2 == Identifier.parse("0x000000000042")
                }.forEach {
                    val elem = JSONObject()
                    elem.put("address", it.bluetoothAddress)
                    elem.put("rssi", it.rssi)
                    elem.put("txPower", it.txPower)
                    elem.put("distance", it.distance)
                    elem.put("namespaceId", it.id1)
                    elem.put("instanceId", it.id2)
                    data.add(elem)
                }

            Log.i(TAG, "Filtered beacons: " + data.size)

            data.sortBy {
                        it.getString("instanceId")
                    }
                    val sendRequest = JSONObject()
                    sendRequest.put("timestamp", "" + Date().time)
                    sendRequest.put("uid", preferences.getString(USER_STR, "null"))
                    sendRequest.put("data", data)

                    Log.i(TAG, "JSON to be sent: $sendRequest")

                    //If USR_TYPE==USER_BASIC, we need to send the beacons in sight of the user
                    if ((preferences.getInt(USR_TYPE, 1)) == USER_BASIC) {
                        uploadMovingData(data, preferences.getString(USER_STR, "null")!!.toInt(), Date().time)
                    } else {    //if USR_TYPE==USER_ADMIN, we need to add a new position
                        uploadNewPoint(sendRequest)
                    }
               }
            }
            try {
                beaconManager.startRangingBeaconsInRegion(region)   //Start the scan
                Log.i(TAG, "Started scan")
            } catch (e: RemoteException) {
            }
        }

    /**
     * Uploads the beacons seen by the smartphone to the server
     */
    private fun uploadMovingData(data: MutableList<JSONObject>, uid: Int, timestamp: Long) {
        HttpPost().newPost("$SRV_URL/api/addMovimento", data, uid, timestamp, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "Failure: ${e.stackTrace}")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.i(TAG, "Data to be sent: $data")
            }
        })
    }

    /**
     * Uploads a new point to the server
     */
    private fun uploadNewPoint(sendRequest : JSONObject) {
        count++
        if (count == 1) {

            HttpPost().run("$SRV_URL/api/nuovoPoint", sendRequest, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d(TAG, "Failure: ${e.stackTrace}")
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.i(TAG, "New point sent to server")
                }
            })
        } else {
            beaconManager.stopRangingBeaconsInRegion(region)
        }
    }
}