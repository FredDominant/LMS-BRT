package com.noblemajesty.brt.views.home


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.noblemajesty.brt.R
import com.noblemajesty.brt.database.entities.BusSchedule
import kotlinx.android.synthetic.main.fragment_trip_payment.*
import java.util.*

class TripPaymentFragment : Fragment() {

    companion object { fun newInstance() = TripPaymentFragment() }

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_trip_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(MainActivityViewModel::class.java)

        val bundle = arguments
        bundle?.let {
            val seatNumber = it.getInt("seatNumber")
            seat.text = seatNumber.toString()
        }
        activity?.actionBar?.title = "CONFIRM TRIP"

        from.text = viewModel.departure
        destination.text = viewModel.destination
        bus.text = viewModel.busName
        departureDate.text = "${viewModel.day}/${viewModel.month}/${viewModel.year}"
        departureTime.text = "${viewModel.hour}:${viewModel.minute}"
        cost.text = Random().nextInt(1000).toString()
        val user = viewModel.getUser()
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
            setPositiveButton("Confirm") { _, _ -> showSuccessModal() }
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun showSuccessModal() {
        val dialogBuilder = AlertDialog.Builder(activity!!)
        dialogBuilder.apply {
            setTitle("Confirm Payment")
            setMessage("Payment confirmed for trip to ${destination.text} from ${from.text} on ${departureDate.text} at ${departureTime.text}")
            setCancelable(true)
            setPositiveButton("close") { _, _ ->
                saveScheduleDetails()
                showPaymentSuccessOptions()
            }
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
            cost = cost.text.toString()
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
}
