package com.noblemajesty.brt.views.home


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.noblemajesty.brt.R
import kotlinx.android.synthetic.main.fragment_add_schedule.*
import java.text.SimpleDateFormat
import java.util.*

class AddScheduleFragment : DialogFragment() {

    private lateinit var viewModel: MainActivityViewModel

    companion object {
        fun newInstance() = AddScheduleFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_schedule, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(MainActivityViewModel::class.java)
        departureDateField.setOnClickListener { _ -> openDateDialog(departureDate) }
        departureDateField.setOnFocusChangeListener { _, hasFocus
            -> if (hasFocus) openDateDialog(departureDate) }

        departureTimeField.setOnClickListener { _ -> openTimeDialog(departureTime) }
        departureTimeField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) openTimeDialog(departureTime) }
        continueBookingButton.setOnClickListener { _ -> onContinueButtonClick() }
    }

    @SuppressLint("SimpleDateFormat")
    private fun openDateDialog(text: TextInputLayout) {
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val format = SimpleDateFormat("dd/MM/yyyy")
            val date = calendar.time
            text.editText?.setText(format.format(date))
            viewModel.year = year
            viewModel.month = month
            viewModel.day = dayOfMonth
        }
        val dateDialog = DatePickerDialog(activity!!, android.R.style.Theme_DeviceDefault_Dialog,
                dateListener, 1990, 1, 1)
        dateDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
        dateDialog.show()
    }

    private fun openTimeDialog(text: TextInputLayout) {
        val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            text.editText?.setText("$hourOfDay: $minute")
            viewModel.hour = hourOfDay
            viewModel.minute = minute
        }
        val timeDialog = TimePickerDialog(activity!!, android.R.style.Theme_DeviceDefault_Dialog,
                timeListener, 1, 1, true)
        timeDialog.show()
    }

    private fun validateFields(): Boolean {
        return (
                tripFrom.editText?.text?.length!! > 0 &&
                tripTo.editText?.text?.length!! > 0 &&
                departureDate.editText?.text?.length!! > 0 &&
                departureTime.editText?.text?.length!! > 0
                )
    }

    private fun onContinueButtonClick() {
        if (validateFields()) {
            viewModel.departure = tripFrom.editText?.text?.toString()
            viewModel.destination = tripTo.editText?.text?.toString()
            val activity = activity!! as? MainActivity
            activity?.goToFragment(AvailableBusesFragment.newInstance(), null)
        } else {
            Snackbar.make(addScheduleContainer, "All Fields are Required.", Snackbar.LENGTH_LONG)
                    .show()
        }
    }

}
