package com.digitalacademy.somjai.ui.payment

import android.content.Context
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
            R.id.generatePaymentBtn -> generatePayment(amount, ref){ generateSuccess ->
                if(generateSuccess) {
                    // CALL TO SERVER

                    // QR String
                    val bitmap = GenerateQR.generate("000201010212304701159784459346017370210REFERENCE10310REFERENCE25204701153037645406100.005802TH5922TestMerchant15919538816007BANGKOK62370523202008131133335920000000706SCBBBB6304D992")
                    qrCodeView.setImageBitmap(bitmap)
                    enableSpinner(false)
                } else {
                    errorToast()
                }
            }
            else -> println("Not found Button")
        }
    }

    private fun generatePayment(amount: String, ref: String, complete: (Boolean) -> Unit) {
        if(amount.isNotEmpty() && ref.isNotEmpty()) {
            convertAmountTo2DecimalString(amount)
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