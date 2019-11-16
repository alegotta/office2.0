package it.lares.smartoffice

import android.app.IntentService
import android.app.Notification
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.RemoteException
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import it.lares.smartoffice.app.Const
import it.lares.smartoffice.app.Const.Companion.ALT_URL
import it.lares.smartoffice.app.Const.Companion.USER_BASIC
import it.lares.smartoffice.app.Const.Companion.USER_STR
import it.lares.smartoffice.app.Const.Companion.USR_TYPE
import it.lares.smartoffice.http.HttpPost
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.altbeacon.beacon.*
import org.altbeacon.beacon.powersave.BackgroundPowerSaver
import org.altbeacon.beacon.service.ArmaRssiFilter
import org.json.JSONObject
import java.io.IOException
import java.util.*
import android.content.Context.NOTIFICATION_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.app.NotificationManager
import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



/**
 * Service used to scan for bluetooth beacons
 */
class BluetoothScanService : IntentService("BluetoothScanService"), BeaconConsumer {

    private lateinit var beaconManager: BeaconManager
    private val TAG = "BluetoothScanService"
    private lateinit var preferences: SharedPreferences
    private var count = 0   //Counter for the insertPoint request
    private val region = Region("Range", null, null, null)

    /**
     * Initialize the service. From android 8 on, it is required to show a notification in order to keep the
     *   service running
     */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val builder = NotificationCompat.Builder(this, "noti_channel")
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Scanner")
                .setSmallIcon(R.mipmap.icon_doc)

        val notification: Notification = builder.build()

        startForeground(1, notification)
        return super.onStartCommand(intent, flags, startId)
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
        beaconManager.backgroundScanPeriod = 2000   //Scan every 2 seconds

        beaconManager.bind(this)

        preferences = getSharedPreferences(Const.PREF_FILE, AppCompatActivity.MODE_PRIVATE)

        Log.i(TAG, "Started bluetooth scan")
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

                    Log.i(TAG, "JSON to be sent:" + sendRequest.toString())

                    //If USR_TYPE==USER_BASIC, we need to send the beacons in sight of the user
                    if ((preferences.getInt(USR_TYPE, 1)) == USER_BASIC) {
                        uploadMovingData(data, preferences.getString(USER_STR, "null").toInt(), Date().time)
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
    fun uploadMovingData(data: MutableList<JSONObject>, uid: Int, timestamp: Long) {
        HttpPost().newPost("$ALT_URL/api/addMovimento", data, uid, timestamp, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "Failure: ${e.stackTrace}")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.i(TAG, "Moving data sent to server: " + data.toString())
            }
        })
    }

    /**
     * Uploads a new point to the server
     */
    fun uploadNewPoint(sendRequest : JSONObject) {
        count++
        if (count == 1) {

            /*HttpPost().run("$ALT_URL/api/nuovoPoint", sendRequest, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d(TAG, "Failure: ${e.stackTrace}")
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.i(TAG, "New point sent to server")
                }
            })*/
        } else {
            beaconManager.stopRangingBeaconsInRegion(region)
        }
    }
}