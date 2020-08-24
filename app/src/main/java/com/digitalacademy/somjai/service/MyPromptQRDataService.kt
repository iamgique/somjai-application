package com.digitalacademy.somjai.service

import android.R
import android.graphics.BitmapFactory

object MyPromptQRDataService {
    var myPromptQR = BitmapFactory.decodeResource(null, R.id.background)
    var rawMyPromptQRText = ""

    // Response Success
    var partnerTransactionId = ""
    var transactionId = ""
    var transactionDateTime = ""
    var exchangeRate = ""
    var transactionAmount = ""
    var transactionCurrency = ""
    var equivalenceTransactionAmount = ""
    var equivalenceTransactionCurrency = ""
    var payerBankCode = ""
    var payerTepaCode = ""
    var billerId = ""
    var reference1 = ""
    var slipId = ""
    var originalTransactionMessageIdentification = ""
    var originalTransactionMessageNameIdentification = ""
    var originalTransactionPaymentInformationIdentification = ""
}