package com.noblemajesty.brt.views.home


import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.noblemajesty.brt.R
import com.noblemajesty.brt.Utils
import com.noblemajesty.brt.Utils.PREFERENCE
import com.noblemajesty.brt.database.entities.User
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {
    private lateinit var viewModel: MainActivityViewModel

    companion object { fun newInstance() = ProfileFragment() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(MainActivityViewModel::class.java)
        updateProfile.setOnClickListener { _ -> updateProfile() }
        val user = viewModel.getUser()
        user?.let {
            profileEmail.editText?.setText(it.email)
            profileFirstName.editText?.setText(it.firstName)
            profileLastName.editText?.setText(it.lastName)
            profilePassword.editText?.setText(it.password)
        }
    }

    private fun validateField() : Boolean {
        return (
                profileEmail.editText?.text?.length!! > 0 &&
                profilePassword.editText?.text?.length!! > 0 &&
                profileFirstName.editText?.text?.length!! > 0 &&
                profileLastName.editText?.text?.length!! > 0
                )
    }

    private fun updateProfile() {
        if (validateField()) {
            viewModel.getUser()?.apply {
                firstName = profileFirstName.editText?.text?.toString()!!
                lastName = profileLastName.editText?.text?.toString()!!
                email = profileEmail.editText?.text?.toString()!!
                password = profilePassword.editText?.text?.toString()!!
                val updateStatus = viewModel.updateProfile(this)
                if (updateStatus) {
                    updateSharedPreference(this)
                    displaySnackbar("Profile updated")
                } else displaySnackbar(viewModel.errorMessage)
            }
        } else { displaySnackbar("All Fields are Required") }
    }

    private fun updateSharedPreference(user: User) {
        activity!!.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).edit()
                .putString(Utils.email, user.email)
                .putString(Utils.firstName, user.firstName)
                .putString(Utils.lastName, user.lastName)
                .apply()
    }

    private fun displaySnackbar(message: String) {
        Snackbar.make(profileContainer, message.toUpperCase(), Snackbar.LENGTH_LONG)
                .show()
    }

}
