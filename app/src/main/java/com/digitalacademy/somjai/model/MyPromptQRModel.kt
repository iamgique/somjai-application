package com.digitalacademy.somjai.model

class MyPromptQRModel {

    data class myPromptQRInfo(
        val partnerTransactionId: String,
        val transactionId: String,
        val transactionDateTime: String,
        val exchangeRate: String,
        val transactionAmount: String,
        val transactionCurrency: String,
        val equivalenceTransactionAmount: String,
        val equivalenceTransactionCurrency: String,
        val payerBankCode: String,
        val payerTepaCode: String,
        val billerId: String,
        val reference1: String,
        val slipId: String,
        val originalTransaction: OriginalTransaction
    )

    data class OriginalTransaction(
        val messageIdentification: String,
        val messageNameIdentification: String,
        val paymentInformationIdentification: String
    )
}