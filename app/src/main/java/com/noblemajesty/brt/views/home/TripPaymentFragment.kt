package com.noblemajesty.brt.views.home


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import co.paystack.android.Paystack
import co.paystack.android.PaystackSdk
import co.paystack.android.Transaction
import co.paystack.android.model.Card
import co.paystack.android.model.Charge

import com.noblemajesty.brt.R
import com.noblemajesty.brt.Utils.month
import com.noblemajesty.brt.Utils.year
import com.noblemajesty.brt.database.entities.BusSchedule
import com.noblemajesty.brt.database.entities.User
import kotlinx.android.synthetic.main.fragment_trip_payment.*
import java.util.*

class TripPaymentFragment : Fragment() {

    companion object { fun newInstance() = TripPaymentFragment() }

    private var seatNumber: Int? = null
    private lateinit var viewModel: MainActivityViewModel
    private var user: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_trip_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(MainActivityViewModel::class.java)

        val bundle = arguments
        bundle?.let {
            seatNumber = it.getInt("seatNumber")
            seatNumber?.let { seatNumber -> seat.text = seatNumber.toString() }
        }

        from.text = viewModel.departure
        destination.text = viewModel.destination
        bus.text = viewModel.busName
        departureDate.text = "${viewModel.day}/${viewModel.month}/${viewModel.year}"
        departureTime.text = "${viewModel.hour}:${viewModel.minute}"
        cost.text = Random().nextInt(1000).toString()
        user = viewModel.getUser()
        user?.let {
            passenger.text = "${it.firstName} ${it.lastName}"
        }

        confirmPaymentButton.setOnClickListener { _ -> showConfirmModal() }
        continueToHome.setOnClickListener { _ -> goToHomeFragment() }
        cancelPayment.setOnClickListener { _ -> showCancelTripWarningDialog() }
    }

    private fun showConfirmModal() {
        val dialogBuilder = AlertDialog.Builder(activity!!)
        dialogBuilder.apply {
            setTitle("Confirm Payment")
            setMessage("Confirm Payment of ${cost.text}?")
            setCancelable(true)
            setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            setPositiveButton("Confirm") { _, _ -> showPaymentDialog() }
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun showCancelTripWarningDialog() {
        val dialogBuilder = AlertDialog.Builder(activity!!)
        dialogBuilder.apply {
            setTitle("Cancel Payment")
            setMessage("Are you sure you want to cancel this trip?")
            setCancelable(true)
            setPositiveButton("Yes, cancel") { _, _ -> showCancelDialog() }
            setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun showCancelDialog() {
        val dialogBuilder = AlertDialog.Builder(activity!!)
        dialogBuilder.apply {
            setTitle("Cancel Payment")
            setMessage("Trip to ${destination.text} from ${from.text} on ${departureDate.text} at ${departureTime.text} has been cancelled")
            setCancelable(false)
            setPositiveButton("Go To Home") { _, _ -> cancelSchedule(); goToHomeFragment() }
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun showPaymentDialog() {
        val inflater = LayoutInflater.from(activity)
        val dialogView = inflater.inflate(R.layout.partial_dialog, null)
        val dialogBuilder = AlertDialog.Builder(activity!!)
        dialogBuilder.apply {
            setView(dialogView)
            setCancelable(true)
            setPositiveButton("Close") { dialog, _ -> dialog.cancel() }
        }
        val dialog = dialogBuilder.create()
        dialog.show()

        dialogView.findViewById<Button>(R.id.paymentButton).setOnClickListener { _ ->
            val cardNumber = dialogView.findViewById<TextInputLayout>(R.id.cardNumber).editText?.text?.toString()
            val cvv = dialogView.findViewById<TextInputLayout>(R.id.cardCVV).editText?.text?.toString()
            val year = dialogView.findViewById<TextInputLayout>(R.id.cardYear).editText?.text?.toString()
            val month = dialogView.findViewById<TextInputLayout>(R.id.cardMonth).editText?.text?.toString()

            validateCard(cardNumber, cvv, month, year)
            dialog.cancel()
        }
    }

    private fun validateCard(cardNumber: String?, cvv: String?, month: String?, year: String?) {
        if (validateInputs(cardNumber, cvv, year, month)) {
            val card = Card(cardNumber, month?.toInt(), year?.toInt(), cvv)
            if (card.isValid) makePayment(card) else {
                Snackbar.make(makePaymentContainer, "Payment card is Invalid", Snackbar.LENGTH_LONG)
                        .show()
            }
        } else {
            Snackbar.make(makePaymentContainer, "All fields are required", Snackbar.LENGTH_LONG)
                    .show()
        }
    }

    private fun makePayment(card: Card) = PaystackSdk
            .chargeCard(activity, getCharge(card), getTransactionCallback())

    private fun getCharge(card: Card) = Charge().apply {
        user?.let {
            email = it.email.trim()
            this.card = card
            amount = cost.text.toString().toInt()
        }
    }

    private fun showPaymentResultDialog(success: Boolean, errorMessage: String?) {
        val inflater = LayoutInflater.from(activity)
        val dialogView = inflater.inflate(R.layout.partial_dialog, null)
        val dialogBuilder = AlertDialog.Builder(activity!!)
        dialogBuilder.apply {
            setView(dialogView)
            setCancelable(true)
            setPositiveButton("Close") { dialog, _ -> dialog.cancel() }
        }
        val dialog = dialogBuilder.create()
        if (success) simulateSuccess(dialogView) else simulateError(dialogView, errorMessage)
        dialog.show()
    }

    private fun simulateSuccess(view: View) {
        view.findViewById<ConstraintLayout>(R.id.paymentMain).visibility = View.GONE
        view.findViewById<ConstraintLayout>(R.id.paymentResult).visibility = View.VISIBLE
        view.findViewById<LinearLayout>(R.id.errorContainer).visibility = View.GONE
        view.findViewById<LinearLayout>(R.id.successContainer).visibility = View.VISIBLE
    }

    private fun simulateError(view: View, errorMessage: String?) {
        view.findViewById<ConstraintLayout>(R.id.paymentMain).visibility = View.GONE
        view.findViewById<ConstraintLayout>(R.id.paymentResult).visibility = View.VISIBLE
        view.findViewById<LinearLayout>(R.id.errorContainer).visibility = View.VISIBLE
        view.findViewById<LinearLayout>(R.id.successContainer).visibility = View.GONE
        view.findViewById<TextView>(R.id.paymentErrorText).text = errorMessage
    }

    private fun goToHomeFragment() {
        val activity = activity as? MainActivity
        activity?.goToFragment(RecentSchedulesFragment.newInstance(), null)
    }

    private fun saveScheduleDetails(): Long? {
        val busSchedule = BusSchedule(
            busName = viewModel.busName!!,
            from = viewModel.departure!!,
            status = "Confirmed",
            userId = viewModel.userId!!,
            destination = viewModel.destination!!,
            date = "${viewModel.day}/${viewModel.month}/${viewModel.year}",
            cost = cost.text.toString(),
            seatNumber = (if (seatNumber != null) seatNumber else 0)!!
        )
        return viewModel.addBusSchedule(busSchedule)
    }

    private fun cancelSchedule() {
        val schedule = viewModel.getBusSchedule(viewModel.scheduleId!!)
        schedule?.status = "Cancelled"
        viewModel.updateSchedule(schedule!!)
    }

    private fun showPaymentSuccessOptions() {
        confirmPaymentButton.visibility = View.GONE
        successfulPaymentOptions.visibility = View.VISIBLE
    }

    private fun getTransactionCallback() : Paystack.TransactionCallback = object : Paystack.TransactionCallback {
        override fun onSuccess(transaction: Transaction?) {
            Log.e("onSuccess", "${transaction?.reference}")
            saveScheduleDetails()
            showPaymentSuccessOptions()
            showPaymentResultDialog(true, null)
        }

        override fun beforeValidate(transaction: Transaction?) {}

        override fun onError(error: Throwable?, transaction: Transaction?) {
            confirmPaymentButton.text = "Retry"
            showPaymentResultDialog(false, error?.message)
        }
    }

    private fun validateInputs(cardNumber: String?, cvv: String?, year: String?, month: String?): Boolean {
        return (
                cardNumber?.length!! > 0 &&
                cvv?.length!! > 0 &&
                year?.length!! > 0 &&
                month?.length!! > 0
                )
    }
}
