package com.digitalacademy.somjai.service

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.digitalacademy.somjai.controller.App
import com.digitalacademy.somjai.model.MyPromptQRModel
import com.digitalacademy.somjai.util.URL_MY_PROMPT_QR
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

object MyPromptQRService {

    fun myPropmtQR(rawMyPromptQR: String, amount: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("qrData", rawMyPromptQR)
        jsonBody.put("transactionAmount", amount)
        val requestBody = jsonBody.toString()

        val myPromptQRRequest = object: JsonObjectRequest(Method.POST, URL_MY_PROMPT_QR, null, Response.Listener { response ->
            try {
                var resp = Gson().fromJson(response.getJSONObject("data").toString(), MyPromptQRModel.myPromptQRInfo::class.java)
                MyPromptQRDataService.partnerTransactionId = resp.partnerTransactionId
                MyPromptQRDataService.transactionId = resp.transactionId
                MyPromptQRDataService.transactionDateTime = resp.transactionDateTime
                MyPromptQRDataService.exchangeRate = resp.exchangeRate
                MyPromptQRDataService.transactionAmount = resp.transactionAmount
                MyPromptQRDataService.transactionCurrency = resp.transactionCurrency
                MyPromptQRDataService.equivalenceTransactionAmount = resp.equivalenceTransactionAmount
                MyPromptQRDataService.equivalenceTransactionCurrency = resp.equivalenceTransactionCurrency
                MyPromptQRDataService.payerBankCode = resp.payerBankCode
                MyPromptQRDataService.payerTepaCode = resp.payerTepaCode
                MyPromptQRDataService.billerId = resp.billerId
                MyPromptQRDataService.reference1 = resp.reference1
                MyPromptQRDataService.slipId = resp.slipId
                MyPromptQRDataService.originalTransactionMessageIdentification = resp.originalTransaction.messageIdentification
                MyPromptQRDataService.originalTransactionMessageNameIdentification = resp.originalTransaction.messageNameIdentification
                MyPromptQRDataService.originalTransactionPaymentInformationIdentification = resp.originalTransaction.paymentInformationIdentification
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC:" + e.localizedMessage)
                complete(false)
            }
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

        App.prefs.requestQueue.add(myPromptQRRequest)
    }
}