package com.digitalacademy.somjai.service

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.digitalacademy.somjai.controller.App
import com.digitalacademy.somjai.util.URL_CREATE_QRCODE30_PAYMENT
import org.json.JSONException
import org.json.JSONObject

object SlipVerificationService {

    fun slipVerification(transaction: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("transaction", transaction)
        val requestBody = jsonBody.toString()

        val slipVerificationRequest = object: JsonObjectRequest(Method.POST, URL_CREATE_QRCODE30_PAYMENT, null, Response.Listener { response ->
            /*try {
                val qrRawData = response.getJSONObject("data").getString("qrRawData")
                PaymentDataService.qrPayment = GenerateQR.generate(qrRawData)
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC:" + e.localizedMessage)
                complete(false)
            }*/
        }, Response.ErrorListener { error ->
            // this is where we deal with our error
            Log.d("ERROR", "Could not get slip verification from service: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(slipVerificationRequest)
    }
}