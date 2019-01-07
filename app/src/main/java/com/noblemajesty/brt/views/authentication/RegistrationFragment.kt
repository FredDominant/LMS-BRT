package com.noblemajesty.brt.views.authentication

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.noblemajesty.brt.R
import com.noblemajesty.brt.views.home.MainActivity
import kotlinx.android.synthetic.main.registration_fragment.*

class RegistrationFragment : Fragment() {

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    private lateinit var viewModel: AuthenticationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.registration_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(AuthenticationViewModel::class.java)
        registerButton.setOnClickListener { _ -> goToMainScreen() }
        loginText.setOnClickListener { _ -> goToLogin() }
    }

    private fun goToLogin() {
        activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance())
                .commit()
    }

    private fun goToMainScreen() {
        if (validateFields()) {
            if (viewModel.signup(firstName.editText?.text!!.toString().trim(),
                            lastName.editText?.text!!.toString().trim(),
                            email.editText?.text!!.toString().trim(),
                            password.editText?.text!!.toString().trim())) {
                (activity!! as? RegistrationActivity)
                        ?.saveDetailsToSharedPreference(
                                email = viewModel.email,
                                lastName = viewModel.lastName,
                                firstName = viewModel.firstName,
                                userId = viewModel.userId
                        )
                Intent(activity, MainActivity::class.java).apply { startActivity(this) }
            } else {
                Snackbar.make(registrationParent, viewModel.errorMessage.toUpperCase(),
                        Snackbar.LENGTH_LONG).show()
            }

        } else {
            Snackbar.make(registrationParent, "All fields are required.".toUpperCase(),
                    Snackbar.LENGTH_LONG).show()
        }
    }

    private fun validateFields(): Boolean {
        return (
                email.editText?.text?.trim()?.length!! > 0 &&
                password.editText?.text?.trim()?.length!! > 0 &&
                firstName.editText?.text?.trim()?.length!! > 0 &&
                lastName.editText?.text?.trim()?.length!! > 0
                )
    }

}
