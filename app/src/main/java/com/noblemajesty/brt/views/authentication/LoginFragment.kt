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
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: AuthenticationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(AuthenticationViewModel::class.java)
        signupText.setOnClickListener { _ -> goToSignup() }
        loginButton.setOnClickListener { _ -> login() }
    }

    private fun goToSignup() {
        activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.container, RegistrationFragment.newInstance())
                .commit()
    }

    private fun validateFields(): Boolean {
        return (
                loginEmail.editText?.text?.length!! > 0 &&
                loginPassword.editText?.text?.length!! > 0
                )
    }

    private fun login() {
        if (validateFields()) {
            if (viewModel.loginUser(loginEmail.editText?.text!!.toString(),
                            loginPassword.editText?.text!!.toString())) {
                (activity!! as? RegistrationActivity)
                        ?.saveDetailsToSharedPreference(
                                email = viewModel.email,
                                lastName = viewModel.lastName,
                                firstName = viewModel.firstName
                        )
                Intent(activity!!, MainActivity::class.java).apply { startActivity(this) }
            } else {
                Snackbar.make(loginContainer, viewModel.errorMessage.toUpperCase(),
                        Snackbar.LENGTH_LONG)
                        .show()
            }

        } else {
            Snackbar.make(loginContainer, "All fields are required.".toUpperCase(),
                    Snackbar.LENGTH_LONG)
                    .show()
        }
    }
}
