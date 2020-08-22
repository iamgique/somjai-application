package com.digitalacademy.somjai.service

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.digitalacademy.somjai.controller.App
import com.digitalacademy.somjai.model.SlipVerificationModel
import com.digitalacademy.somjai.util.URL_SLIP_VERIFICATION
import com.google.gson.Gson
import org.json.JSONException

object SlipVerificationService {

    fun slipVerification(transaction: String, complete: (Boolean) -> Unit) {
        val pureTransaction = transaction.takeLast(37).take(23)
        val slipVerificationRequest = object: JsonObjectRequest(Method.GET, "$URL_SLIP_VERIFICATION${pureTransaction}?sendingBank=014", null, Response.Listener { response ->
            try {
                var resp = Gson().fromJson(response.getJSONObject("data").toString(), SlipVerificationModel.SlipVerificationInfo::class.java)
                SlipVerificationDataService.transRef = resp.transRef
                SlipVerificationDataService.sendingBank = resp.sendingBank
                SlipVerificationDataService.receivingBank = resp.receivingBank
                SlipVerificationDataService.transDate = resp.transDate
                SlipVerificationDataService.transTime = resp.transTime
                SlipVerificationDataService.senderDisplayName = resp.sender.displayName
                SlipVerificationDataService.senderName = resp.sender.name
                SlipVerificationDataService.senderProxyType = resp.sender.proxy.type
                SlipVerificationDataService.senderProxyValue = resp.sender.proxy.value
                SlipVerificationDataService.senderAccountType = resp.sender.account.type
                SlipVerificationDataService.senderAccountValue = resp.sender.account.value
                SlipVerificationDataService.receiverDisplayName = resp.receiver.displayName
                SlipVerificationDataService.receiverName = resp.receiver.name
                SlipVerificationDataService.receiverProxyType = resp.receiver.proxy.type
                SlipVerificationDataService.receiverProxyValue = resp.receiver.proxy.value
                SlipVerificationDataService.receiverAccountType = resp.receiver.account.type
                SlipVerificationDataService.receiverAccountValue = resp.receiver.account.value
                SlipVerificationDataService.amount = resp.amount
                SlipVerificationDataService.paidLocalAmount = resp.paidLocalAmount
                SlipVerificationDataService.paidLocalCurrency = resp.paidLocalCurrency
                SlipVerificationDataService.ref1 = resp.ref1
                SlipVerificationDataService.ref2 = resp.ref2
                SlipVerificationDataService.ref3 = resp.ref3
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

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(slipVerificationRequest)
    }
}