package it.lares.smartoffice.http

import it.lares.smartoffice.Application
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


/**
 * Makes an HTTP request
 */
class HttpRequest {
    private val client = OkHttpClient()

    fun run(url: String, callback: Callback) {
        val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(callback)
    }
}

/**
 * Send a POST request using JSON
 */
class HttpPost {
    private val client = OkHttpClient()

    fun run(url: String, json: JSONObject, callback: Callback): Call {
        val JSON : MediaType = "application/json; charset=utf-8".toMediaType()

        val body = json.toString().toRequestBody(JSON)
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun newPost(url: String, data: MutableList<JSONObject>, userId: Int, timestamp: Long, callback: Callback): Call {
        val JSON : MediaType = "application/json; charset=utf-8".toMediaType()

        val body = data.toString().toRequestBody(JSON)
        val request = Request.Builder()
                .url("$url?uid=$userId&timestamp=$timestamp")
                .post(body)
                .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }
}