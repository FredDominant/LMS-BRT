package com.noblemajesty.brt.views.home


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.noblemajesty.brt.R
import com.noblemajesty.brt.adapters.BusSeatAdapter
import kotlinx.android.synthetic.main.fragment_bus_seat.*

class BusSeatFragment : Fragment() {

    private lateinit var viewModel: MainActivityViewModel

    var name: String? = null
    var color: String? = null
    var capacity: String? = null

    companion object { fun newInstance() = BusSeatFragment() }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bus_seat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(MainActivityViewModel::class.java)
        selectSeatBusFrom.text = viewModel.departure
        selectSeatBusDestination.text = viewModel.destination

        arguments?.let {
            name = it.getString("busName")
            color = it.getString("busColor")
            capacity = it.getString("busCapacity")

            selectSeatBusCapacity.text = capacity
            selectSeatBusColor.text = color
            selectSeatBusName.text = name
            viewModel.busName = name
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val busSeats = viewModel.populateBusSeats(capacity?.toInt()!!)
        val adapter = BusSeatAdapter(busSeats)
        val layoutManager = GridLayoutManager(activity!!, 4)
        busSeatList.apply {
            setHasFixedSize(true)
            this.adapter = adapter
            this.layoutManager = layoutManager
        }
    }

}
