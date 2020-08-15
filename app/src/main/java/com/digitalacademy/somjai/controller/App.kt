package com.digitalacademy.somjai.controller

import android.app.Application
import com.digitalacademy.somjai.util.SharedPrefs

class App: Application() {
    companion object {
        lateinit var prefs: SharedPrefs
    }

    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}