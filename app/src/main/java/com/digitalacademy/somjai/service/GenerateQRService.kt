package com.digitalacademy.somjai.service

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.digitalacademy.somjai.controller.App
import com.digitalacademy.somjai.util.URL_CREATE_QRCODE30_PAYMENT
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import org.json.JSONException
import org.json.JSONObject

object GenerateQRService {

    fun getQRCode(amount: String, ref: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("amount", amount)
        jsonBody.put("ref3", ref)
        val requestBody = jsonBody.toString()

        val qrPaymentRequest = object: JsonObjectRequest(Method.POST, URL_CREATE_QRCODE30_PAYMENT, null, Response.Listener { response ->
            try {
                val qrRawData = response.getJSONObject("data").getString("qrRawData")
                PaymentDataService.qrPayment = generate(qrRawData)
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC:" + e.localizedMessage)
                complete(false)
            }
        }, Response.ErrorListener { error ->
            // this is where we deal with our error
            Log.d("ERROR", "Could not getQRCode from service: $error")
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

        App.prefs.requestQueue.add(qrPaymentRequest)
    }

    fun generate(content: String) : Bitmap {
        //val content = "bitcoin:3J98t1WpEZ73CNmQviecrnyiWrnqRhWNLy"
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 768, 768)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }
}