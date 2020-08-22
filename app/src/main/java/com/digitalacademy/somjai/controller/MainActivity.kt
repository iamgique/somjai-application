package com.digitalacademy.somjai.controller

import android.Manifest
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.digitalacademy.somjai.R
import com.digitalacademy.somjai.model.PaymentConfirmModel
import com.digitalacademy.somjai.service.AuthService
import com.digitalacademy.somjai.service.UserDataService
import com.digitalacademy.somjai.util.BROADCAST_USER_DATA_CHANGE
import com.digitalacademy.somjai.util.SOCKET_URL
import com.digitalacademy.somjai.util.SUBSCRIBE_PAYMENT_SUCCEED
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header_main.*


class MainActivity : AppCompatActivity() {

    val socket = IO.socket(SOCKET_URL)
    private val TAG = "Permission"
    private val RECORD_REQUEST_CODE = 101

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home,
            R.id.nav_payment,
            R.id.nav_my_prompt,
            R.id.nav_slip_verification,
            R.id.nav_about_us
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        socket.connect()
        socket.on(SUBSCRIBE_PAYMENT_SUCCEED, onPaymentSucceed)

        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver,
            IntentFilter(BROADCAST_USER_DATA_CHANGE)
        )

        if(App.prefs.isLoggedIn) {
            AuthService.findUserByEmail(this) {}
        }
        setupPermissions()
    }

    override fun onRestart() {
        super.onRestart()
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter(
            BROADCAST_USER_DATA_CHANGE)
        )
        socket.connect()
    }

    override fun onDestroy() {
        socket.disconnect()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    private val onPaymentSucceed = Emitter.Listener { args ->
        if (App.prefs.isLoggedIn) {
            runOnUiThread {
                if(args != null) {
                    var resp = Gson().fromJson(args[0].toString(), PaymentConfirmModel.PaymentConfirmInfo::class.java)
                    Log.d("onPaymentSucceed", resp.toString())

                    val builder: AlertDialog.Builder? = this.let { AlertDialog.Builder(it) }
                    builder?.setMessage(
                            Html.fromHtml("<b>Slip Verification Info</b><br />" +
                                    "<b>Payee</b><br />" +
                                    "<b>Proxy Id</b>: ${resp.payeeProxyId}<br />" +
                                    "<b>Proxy Type</b>: ${resp.payeeProxyType}<br />" +
                                    "<b>Account Number</b>: ${resp.payeeAccountNumber}<br />" +
                                    "<b>Name</b>: ${resp.payeeName}<br />" +
                                    "<br />" +
                                    "<b>Payer</b><br />" +
                                    "<b>Proxy Id</b>: ${resp.payerProxyId}<br />" +
                                    "<b>Proxy Type</b>: ${resp.payerProxyType}<br />" +
                                    "<b>Account Number</b>: ${resp.payerAccountNumber}<br />" +
                                    "<b>Name</b>: ${resp.payerName}<br />" +
                                    "<br />" +
                                    "<b>Sending Bank Code</b></b>: ${resp.sendingBankCode}<br />" +
                                    "<b>Receiving Bank Code</b>: ${resp.receivingBankCode}<br />" +
                                    "<b>Amount</b>: ${resp.amount}<br />" +
                                    "<b>Channel Code</b>: ${resp.channelCode}<br />" +
                                    "<b>Currency Code</b>: ${resp.currencyCode}<br />" +
                                    "<br />" +
                                    "<b>Transaction</b><br />" +
                                    "<b>Id</b>: ${resp.transactionId}<br />" +
                                    "<b>Date and Time</b>: ${resp.transactionDateandTime}<br />" +
                                    "<b>Type</b>: ${resp.transactionType}<br />" +
                                    "<br />" +
                                    "<b>Bill Payment Ref</b><br />" +
                                    "<b>Ref1</b>: ${resp.billPaymentRef1}<br />" +
                                    "<b>Ref2</b>: ${resp.billPaymentRef2}<br />" +
                                    "<b>Ref3</b>: ${resp.billPaymentRef3}<br />"
                                ))
                        ?.setTitle(Html.fromHtml("<b>Payment Succeed</b>"))
                    builder?.setPositiveButton("OK", null)

                    val dialog: AlertDialog? = builder?.create()
                    dialog!!.show()

                    val button = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    val textMessageView = dialog.findViewById<View>(android.R.id.message) as TextView
                    button.setTextColor(Color.WHITE)
                    button.setBackgroundColor(Color.DKGRAY)
                    textMessageView.textSize = 16f
                }
            }
        }

    }

    private val userDataChangeReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (App.prefs.isLoggedIn) {
                userNameNavHeader.text = UserDataService.name
                userEmailNavHeader.text = UserDataService.email
                val resourceId = resources.getIdentifier(UserDataService.avatarName, "drawable",
                    packageName)
                userImageNavHeader.setImageResource(resourceId)
                userImageNavHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
                loginBtnNav.text = "Logout"
                text_home.text = "Welcome to Somjai Application"

                val navView: NavigationView = findViewById(R.id.nav_view)
                val menu: Menu = navView.getMenu()
                for (menuItemIndex in 0 until menu.size()) {
                    val menuItem: MenuItem = menu.getItem(menuItemIndex)
                    if(menuItem.getItemId() === R.id.nav_menu_payment) {
                        menuItem.isVisible = true
                    }
                    if(menuItem.getItemId() === R.id.nav_menu_verification) {
                        menuItem.isVisible = true
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun loginBtnNavClicked(view: View) {
        if(App.prefs.isLoggedIn) {
            UserDataService.logout()
            userNameNavHeader.text = ""
            userEmailNavHeader.text = ""
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
            loginBtnNav.text = "Login"
            text_home.text = "Welcome to Somjai Application \nPlease Login"

            val navView: NavigationView = findViewById(R.id.nav_view)
            val menu: Menu = navView.getMenu()
            for (menuItemIndex in 0 until menu.size()) {
                val menuItem: MenuItem = menu.getItem(menuItemIndex)
                if(menuItem.getItemId() === R.id.nav_menu_payment) {
                    menuItem.isVisible = false
                }
                if(menuItem.getItemId() === R.id.nav_menu_verification) {
                    menuItem.isVisible = false
                }
            }
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        } else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    private fun setupPermissions() {
        makeRequest()
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), RECORD_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                }
            }
        }
    }
}