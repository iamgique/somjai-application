package com.digitalacademy.somjai.model

class SlipVerificationModel {
    data class SlipVerificationInfo(
        val transRef: String,
        val sendingBank: String,
        val receivingBank: String,
        val transDate: String,
        val transTime: String,
        val sender: Sender,
        val receiver: Receiver,
        val amount: String,
        val paidLocalAmount: String,
        val paidLocalCurrency: String,
        val ref1: String,
        val ref2: String,
        val ref3: String
    )

    data class Sender(
        val displayName: String,
        val name: String,
        val proxy: Proxy,
        val account: Account
    )

    data class Receiver(
        val displayName: String,
        val name: String,
        val proxy: Proxy,
        val account: Account
    )

    data class Proxy(
        val type: String,
        val value: String
    )

    data class Account(
        val type: String,
        val value: String
    )
}