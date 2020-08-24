package com.digitalacademy.somjai.controller

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.digitalacademy.somjai.R
import com.digitalacademy.somjai.service.*
import kotlinx.android.synthetic.main.activity_my_prompt_qr.*
import kotlinx.android.synthetic.main.fragment_payment.*

class MyPromptQRActivity : AppCompatActivity() {

    lateinit var myPromptQRAdapter: ArrayAdapter<String>

    fun generateMyPromptQRBtnClicked(view: View) {
        enableSpinner(true)
        hideKeyboard()
        amountMyPromptQRTxt.isEnabled = false
        generateMyPromptQRBtn.isEnabled = false
        generateMyPromptQRBtn.isClickable = false
        generateMyPromptQRBtn.setBackgroundColor(Color.parseColor("#666666"))

        val amount = amountMyPromptQRTxt.text.toString()
        val rawMyPromptQR = MyPromptQRDataService.rawMyPromptQRText
        if(amount.isNotEmpty() && rawMyPromptQR.isNotEmpty()) {
            MyPromptQRService.myPropmtQR(rawMyPromptQR, amount) { success ->
                if(success) {
                    var list = ArrayList<String>()
                    list.add("My Prompt QR Success")
                    list.add("Partner Transaction Id: " + MyPromptQRDataService.partnerTransactionId)
                    list.add("Transaction Id: " + MyPromptQRDataService.transactionId)
                    list.add("Transaction Date Time: " + MyPromptQRDataService.transactionDateTime)
                    list.add("Exchange Rate: " + MyPromptQRDataService.exchangeRate)
                    list.add("Transaction Amount: " + MyPromptQRDataService.transactionAmount)
                    list.add("Transaction Currency: " + MyPromptQRDataService.transactionCurrency)
                    list.add("Equivalence Transaction Amount: " + MyPromptQRDataService.equivalenceTransactionAmount)
                    list.add("Equivalence Transaction Currency: " + MyPromptQRDataService.equivalenceTransactionCurrency)
                    list.add("Payer Bank Code: " + MyPromptQRDataService.payerBankCode)
                    list.add("Payer Tepa Code: " + MyPromptQRDataService.payerTepaCode)
                    list.add("Biller Id: " + MyPromptQRDataService.billerId)
                    list.add("Reference1: " + MyPromptQRDataService.reference1)
                    list.add("Slip Id: " + MyPromptQRDataService.slipId)
                    list.add("")
                    list.add("Original Transaction")
                    list.add("Message Identification: " + MyPromptQRDataService.originalTransactionMessageIdentification)
                    list.add("Message Name Identification: " + MyPromptQRDataService.originalTransactionMessageNameIdentification)
                    list.add("PaymentInformation Identification: " + MyPromptQRDataService.originalTransactionPaymentInformationIdentification)

                    myPromptQRAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
                    var listView: ListView = this.findViewById(R.id.myPromptQRListView)
                    listView.adapter = myPromptQRAdapter

                    enableSpinner(false)
                } else {
                    errorToast()
                }
            }
        } else {
            amountMyPromptQRTxt.isEnabled = true
            generateMyPromptQRBtn.isEnabled = true
            generateMyPromptQRBtn.isClickable = true
            generateMyPromptQRBtn.setBackgroundColor(Color.parseColor("#FF424242"))
            Toast.makeText(this, "Please fill in amount and scan QR", Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_prompt_qr)
        myPromptQRSpinner.visibility = View.INVISIBLE
    }

    fun errorToast() {
        amountMyPromptQRTxt.isEnabled = true
        generateMyPromptQRBtn.isEnabled = true
        generateMyPromptQRBtn.isClickable = true
        generateMyPromptQRBtn.setBackgroundColor(Color.parseColor("#FF424242"))
        Toast.makeText(this, "Something went wrong, please try again.", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean) {
        if(enable) {
            myPromptQRSpinner.visibility = View.VISIBLE
        } else {
            myPromptQRSpinner.visibility = View.INVISIBLE
        }
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }
}