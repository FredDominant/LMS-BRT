package com.noblemajesty.brt.views.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.noblemajesty.brt.R

class AvailableBusesFragment : Fragment() {

    private var dayOfMonth: Int? = null
    private var month: Int? = null
    private var year: Int? = null

    companion object { fun newInstance() = AvailableBusesFragment() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_available_buses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
