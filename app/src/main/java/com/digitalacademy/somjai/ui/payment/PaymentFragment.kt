package com.digitalacademy.somjai.ui.payment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.digitalacademy.somjai.R
import com.digitalacademy.somjai.service.GenerateQR
import com.digitalacademy.somjai.service.PaymentDataService
import kotlinx.android.synthetic.main.fragment_payment.*

class PaymentFragment : Fragment(), View.OnClickListener {

    private lateinit var paymentViewModel: PaymentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        paymentViewModel = ViewModelProviders.of(this).get(PaymentViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_payment, container, false)
        val textView: TextView = root.findViewById(R.id.text_payment)
        paymentViewModel.text.observe(viewLifecycleOwner, Observer { textView.text = it })

        val generatePaymentBtn: Button = root.findViewById(R.id.generatePaymentBtn)
        generatePaymentBtn.setOnClickListener(this)

        return root
    }

    override fun onClick(view: View) {
        enableSpinner(true)
        hideKeyboard()

        val amount = amountTxt.text.toString()
        val ref = refTxt.text.toString()

        when(view.id) {
            R.id.generatePaymentBtn -> checkGeneratePayment(amount, ref){ canGenerate ->
                if(canGenerate) {
                    GenerateQR.getQRCode(amount, ref) { generateSuccess ->
                        if(generateSuccess) {
                            // QR String
                            qrCodeView.setImageBitmap(PaymentDataService.qrPayment)
                            enableSpinner(false)
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }
            }
            else -> println("Not found Button")
        }
    }

    private fun checkGeneratePayment(amount: String, ref: String, complete: (Boolean) -> Unit) {
        if(amount.isNotEmpty() && ref.isNotEmpty()) {
            convertAmountTo2DecimalString(amount)
            amountTxt.isEnabled = false
            refTxt.isEnabled = false
            generatePaymentBtn.isEnabled = false
            generatePaymentBtn.isClickable = false
            generatePaymentBtn.setBackgroundColor(Color.parseColor("#666666"))
            complete(true)
        } else {
            println("Make sure user name, email, and password are filled in.")
            Toast.makeText(activity, "Make sure user name, email, and password are filled in.",
                Toast.LENGTH_LONG).show()
            complete(false)
        }
    }

    private fun convertAmountTo2DecimalString(amount: String) {
        amountTxt.setText(String.format("%.2f", amount.toFloat()))
    }

    private fun enableSpinner(enable: Boolean) {
        if(enable) {
            generatePaymentSpinner.visibility = View.VISIBLE
        } else {
            generatePaymentSpinner.visibility = View.INVISIBLE
        }
        generatePaymentBtn.isEnabled = !enable
    }

    fun errorToast() {
        Toast.makeText(activity, "Something went wrong, please try again.", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun hideKeyboard() {
        val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }
}