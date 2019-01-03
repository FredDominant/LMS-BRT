package com.noblemajesty.brt

import android.app.Application
import co.paystack.android.PaystackSdk

class BRTApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        PaystackSdk.initialize(this)
    }
}