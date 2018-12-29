package com.noblemajesty.brt.adapters

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.noblemajesty.brt.BusSeat
import com.noblemajesty.brt.R
import com.noblemajesty.brt.views.home.MainActivity
import com.noblemajesty.brt.views.home.TripPaymentFragment

class BusSeatViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    @SuppressLint("NewApi")
    fun setValues(busSeat: BusSeat) {
        view.findViewById<TextView>(R.id.seatNumber).text = busSeat.number?.toString()
        if (!busSeat.isAvailable!!) {
            view.findViewById<CardView>(R.id.busSeatContainer).setCardBackgroundColor(Color.GRAY)
            view.setOnClickListener {
                Snackbar.make(view.rootView, "Seat is Unavailable", Snackbar.LENGTH_LONG).show()
            }
        } else {
            view.findViewById<CardView>(R.id.busSeatContainer).
                    setCardBackgroundColor(view.context.resources.getColor(R.color.colorPrimary, null))
            view.setOnClickListener {
//                Snackbar.make(view.rootView, "Seat ${busSeat.number} selected", Snackbar.LENGTH_LONG).show()
                displayDialog(busSeat.number!!)
            }
        }
    }

    private fun displayDialog(seatNumber: Int) {
        val dialogBuilder = AlertDialog.Builder(view.context).apply {
            setMessage("Confirm seat $seatNumber for your trip?")
            setTitle("Confirm seat")
            setCancelable(true)
            setPositiveButton("Confirm") { _, _ -> goToNextScreen(seatNumber) }
            setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun goToNextScreen(seatNumber: Int) {
        val activity = view.context as? MainActivity
        val bundle = Bundle().apply { putInt("seatNumber", seatNumber) }
        activity?.goToFragment(TripPaymentFragment.newInstance(), bundle)
    }
}
