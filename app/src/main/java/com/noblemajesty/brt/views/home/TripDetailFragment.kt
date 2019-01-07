package com.noblemajesty.brt.views.home


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.noblemajesty.brt.R
import kotlinx.android.synthetic.main.fragment_trip_detail.*
import kotlinx.android.synthetic.main.fragment_trip_payment.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class TripDetailFragment : Fragment() {

    companion object { fun newInstance() = TripDetailFragment() }
    private lateinit var viewModel: MainActivityViewModel
    private var scheduleId: Long? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(MainActivityViewModel::class.java)
        arguments?.let { bundle ->
            tripDetailFrom.text = bundle.getString("tripFrom")
            tripDetailDestination.text = bundle.getString("tripDestination")
            tripDetailDepartureDate.text = bundle.getString("departure")
            tripDetailSeatNumber.text = bundle.getInt("seatNumber").toString()
            tripDetailCost.text =  bundle.getString("cost")
            tripDetailStatus.text = bundle.getString("status")
            tripDetailBusName.text = bundle.getString("busName")

            scheduleId = bundle.getInt("scheduleId").toLong()

            val user = viewModel.getUser()
            user?.let { tripDetailPassenger.text = "${it.firstName} ${it.lastName}" }
            val departureDate = bundle.getString("departure")
            val splitDate = departureDate?.split("/")

            val calendar = Calendar.getInstance()

            splitDate?.let { splitDate ->
                calendar.set(Calendar.YEAR, splitDate[2].toInt())
                calendar.set(Calendar.MONTH, splitDate[1].toInt())
                calendar.set(Calendar.DAY_OF_MONTH, splitDate[0].toInt())
            }

            val travelDate = calendar.timeInMillis

            val currentDate = System.currentTimeMillis()

            if ((travelDate > currentDate) && bundle.getString("status") != "Cancelled") {
                tripDetailCancelTripButton.visibility = View.VISIBLE
                tripDetailCancelTripButton.setOnClickListener { _ -> showCancelDialog() }
            } else { tripDetailCancelTripButton.visibility = View.GONE }
        }
    }

    private fun cancelTrip(scheduleId: Long): Boolean {
        val schedule = viewModel.getBusSchedule(scheduleId)
        schedule?.let {
            it.status = "Cancelled"
            viewModel.updateSchedule(it)
            return true
        } ?: return false
    }

    private fun showCancelDialog() {
        val dialogBuilder = AlertDialog.Builder(activity!!)
        dialogBuilder.apply {
            setTitle("Cancel Payment")
            setMessage("Are you sure you want to cancel this trip? Charges may apply.")
            setCancelable(false)
            setPositiveButton("Yes, Cancel") { _, _ ->
                scheduleId?.let {
                    if (cancelTrip(it)) {
                        tripDetailStatus.text = "Cancelled"
                        Snackbar
                                .make(tripDetailContainer, "Trip has been cancelled", Snackbar.LENGTH_LONG)
                                .show()
                    } else {
                        Toast
                                .makeText(activity, "An error occurred, please try again later", Toast.LENGTH_LONG)
                                .show()
                    }
                }
            }
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun goToHomeFragment() {
        val activity = (activity as? MainActivity)
        activity?.goToFragment(RecentSchedulesFragment.newInstance(), null)
    }

}
