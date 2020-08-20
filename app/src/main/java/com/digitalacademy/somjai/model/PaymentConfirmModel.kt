package com.digitalacademy.somjai.model

class PaymentConfirmModel {

    data class PaymentConfirmInfo (
        val payeeProxyId: String,
        val payeeProxyType: String,
        val payeeAccountNumber: String,
        val payeeName: String,
        val payerProxyId: String,
        val payerProxyType: String,
        val payerAccountNumber: String,
        val payerName: String,
        val sendingBankCode: String,
        val receivingBankCode: String,
        val amount: String,
        val channelCode: String,
        val transactionId: String,
        val transactionDateandTime: String,
        val billPaymentRef1: String,
        val billPaymentRef2: String,
        val billPaymentRef3: String,
        val currencyCode: String,
        val transactionType: String
    )
}