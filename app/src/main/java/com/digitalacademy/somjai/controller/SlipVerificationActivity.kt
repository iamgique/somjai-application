package com.digitalacademy.somjai.controller

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.digitalacademy.somjai.R
import com.digitalacademy.somjai.service.PaymentDataService
import com.digitalacademy.somjai.service.SlipVerificationDataService
import com.digitalacademy.somjai.service.SlipVerificationService
import kotlinx.android.synthetic.main.activity_slip_verification.*

class SlipVerificationActivity : AppCompatActivity() {

    lateinit var slipVerificationAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slip_verification)
        slipVerificationSpinner.visibility = View.INVISIBLE

        val rawSlip : String = PaymentDataService.rawSlipVerificationText

        SlipVerificationService.slipVerification(rawSlip) {success ->
            if(success) {
                // show data
                var list = ArrayList<String>()
                list.add("Payment Success")
                list.add("Trans Ref: " + SlipVerificationDataService.transRef)
                list.add("Sending Bank: " + SlipVerificationDataService.sendingBank)
                list.add("Receiving Bank: " + SlipVerificationDataService.receivingBank)
                list.add("Trans Date: " + SlipVerificationDataService.transDate)
                list.add("Trans Time: " + SlipVerificationDataService.transTime)
                list.add("")
                list.add("Sender")
                list.add("Display Name: " + SlipVerificationDataService.senderDisplayName)
                list.add("Name: " + SlipVerificationDataService.senderName)
                list.add("Proxy Type: " + SlipVerificationDataService.senderProxyType)
                list.add("Proxy Value: " + SlipVerificationDataService.senderProxyValue)
                list.add("Account Type: " + SlipVerificationDataService.senderAccountType)
                list.add("Account Value: " + SlipVerificationDataService.senderAccountValue)
                list.add("")
                list.add("Receiver")
                list.add("Display Name: " + SlipVerificationDataService.receiverDisplayName)
                list.add("Name: " + SlipVerificationDataService.receiverName)
                list.add("Proxy Type: " + SlipVerificationDataService.receiverProxyType)
                list.add("Proxy Value: " + SlipVerificationDataService.receiverProxyValue)
                list.add("Account Type: " + SlipVerificationDataService.receiverAccountType)
                list.add("Account Value: " + SlipVerificationDataService.receiverAccountValue)
                list.add("")
                list.add("Amount: " + SlipVerificationDataService.amount)
                list.add("Paid Local Amount: " + SlipVerificationDataService.paidLocalAmount)
                list.add("Paid Local Currency: " + SlipVerificationDataService.paidLocalCurrency)
                list.add("")
                list.add("Reference")
                list.add("Ref1: " + SlipVerificationDataService.ref1)
                list.add("Ref2: " + SlipVerificationDataService.ref2)
                list.add("Ref3: " + SlipVerificationDataService.ref3)

                slipVerificationAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
                var listView: ListView = this.findViewById(R.id.slipVerificationListView)
                listView.adapter = slipVerificationAdapter

                enableSpinner(false)
            } else {
                errorToast()
            }
        }

    }

    fun errorToast() {
        Toast.makeText(this, "Something went wrong, please try again.", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean) {
        if(enable) {
            slipVerificationSpinner.visibility = View.VISIBLE
        } else {
            slipVerificationSpinner.visibility = View.INVISIBLE
        }
        //loginLoginBtn.isEnabled = !enable
    }
}