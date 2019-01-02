package com.noblemajesty.brt.views.home


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.noblemajesty.brt.R
import kotlinx.android.synthetic.main.fragment_trip_detail.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class TripDetailFragment : Fragment() {

    companion object { fun newInstance() = TripDetailFragment() }
    private lateinit var viewModel: MainActivityViewModel

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

            val scheduleId = bundle.getInt("scheduleId")

            val user = viewModel.getUser()
            user?.let { tripDetailPassenger.text = "${it.firstName} ${it.lastName}" }
            val departureDate = bundle.getString("departure")
            val dateSplit = departureDate.split("/")

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, dateSplit[2].toInt())
            calendar.set(Calendar.MONTH, dateSplit[1].toInt())
            calendar.set(Calendar.DAY_OF_MONTH, dateSplit[0].toInt())

            val travelDate = calendar.timeInMillis

            val currentDate = System.currentTimeMillis()

            if ((travelDate > currentDate) && bundle.getString("status") != "Cancelled") {
                tripDetailCancelTripButton.visibility = View.VISIBLE
                tripDetailCancelTripButton.setOnClickListener { _ ->
                    if (cancelTrip(scheduleId.toLong())) {
                        // Trip cancelled!
                    } else {
                        // An error occurred
                    }
                }
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
}
