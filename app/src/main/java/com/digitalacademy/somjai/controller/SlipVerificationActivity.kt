package com.digitalacademy.somjai.controller

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.digitalacademy.somjai.R
import com.digitalacademy.somjai.service.PaymentDataService
import com.digitalacademy.somjai.service.SlipVerificationService
import kotlinx.android.synthetic.main.activity_slip_verification.*

class SlipVerificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slip_verification)
        slipVerificationSpinner.visibility = View.INVISIBLE

        val rawSlip : String = PaymentDataService.rawSlipVerificationText
        rawSlipVerificationTxt.text = rawSlip

        SlipVerificationService.slipVerification(rawSlip) {success ->
            if(success) {
                // show data
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

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if(inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }
}