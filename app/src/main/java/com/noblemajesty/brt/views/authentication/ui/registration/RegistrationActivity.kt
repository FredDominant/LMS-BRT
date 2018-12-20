package com.noblemajesty.brt.views.authentication.ui.registration

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.noblemajesty.brt.R

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, RegistrationFragment.newInstance())
                    .commitNow()
        }

    }

}
