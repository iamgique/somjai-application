package com.digitalacademy.somjai.service

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.digitalacademy.somjai.controller.App
import com.digitalacademy.somjai.util.BROADCAST_USER_DATA_CHANGE
import com.digitalacademy.somjai.util.URL_GET_USER
import com.digitalacademy.somjai.util.URL_LOGIN
import org.json.JSONException
import org.json.JSONObject

object AuthService {

    fun loginUser(email: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object: JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener { response ->
            try {
                App.prefs.userEmail = response.getJSONObject("data").getJSONObject("user").getString("username")
                App.prefs.authToken = response.getJSONObject("data").getString("accessToken")
                //App.prefs.userEmail = response.getString("user")
                //App.prefs.authToken = response.getString("token")
                App.prefs.isLoggedIn = true
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC:" + e.localizedMessage)
                complete(false)
            }

        }, Response.ErrorListener { error ->
            // this is where we deal with our error
            Log.d("ERROR", "Could not login user: $error")
            complete(false)
        }) {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        App.prefs.requestQueue.add(loginRequest)
    }

    fun findUserByEmail(context: Context, complete: (Boolean) -> Unit) {
        val findUserRequest = object: JsonObjectRequest(Method.GET, "$URL_GET_USER${App.prefs.userEmail}", null, Response.Listener { response ->

            try {
                UserDataService.name = response.getJSONObject("data").getString("username")
                UserDataService.email = response.getJSONObject("data").getString("email")
                UserDataService.id = response.getJSONObject("data").getString("id")
                //UserDataService.name = response.getString("name")
                //UserDataService.email = response.getString("email")
                //UserDataService.avatarName = response.getString("avatarName")
                //UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.avatarName = "dark19"
                UserDataService.avatarColor = "[0.9647058823529412, 0.9647058823529412, 0.9647058823529412]"
                //UserDataService.id = response.getString("_id")

                val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChange)
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: " + e.localizedMessage)
            }

        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not find user: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(findUserRequest)
    }
}