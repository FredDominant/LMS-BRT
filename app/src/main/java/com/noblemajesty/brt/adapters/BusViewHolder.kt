package com.noblemajesty.brt.adapters

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.noblemajesty.brt.R
import com.noblemajesty.brt.database.entities.Bus
import com.noblemajesty.brt.views.home.MainActivity
import com.noblemajesty.brt.views.home.BusSeatFragment

class BusViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    fun setValues(bus: Bus) {
        view.findViewById<TextView>(R.id.busName).text = bus.name
        view.findViewById<TextView>(R.id.busColor).text = bus.color
        view.findViewById<TextView>(R.id.busCapacity).text = bus.capacity.toString()

        val activity = view.context as? MainActivity
        view.setOnClickListener { _ ->
            val bundle = Bundle().apply {
                putString("busName", bus.name)
                putString("busColor", bus.color)
                putString("busCapacity", bus.capacity.toString())
            }
            activity?.goToFragment(BusSeatFragment.newInstance(), bundle)
        }
    }


}
