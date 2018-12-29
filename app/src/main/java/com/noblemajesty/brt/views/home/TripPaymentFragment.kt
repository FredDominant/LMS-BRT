package com.noblemajesty.brt.views.home


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.noblemajesty.brt.R
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
            setMessage("Payment confirmed for ${departureDate.text} at ${departureTime.text}!")
            setCancelable(true)
            setPositiveButton("close") { _, _ -> goToHomeFragment() }
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun goToHomeFragment() {
        val activity = activity as? MainActivity
        activity?.goToFragment(RecentSchedulesFragment.newInstance(), null)
    }

}
