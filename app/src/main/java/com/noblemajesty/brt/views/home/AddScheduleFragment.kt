package com.noblemajesty.brt.views.home


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.noblemajesty.brt.R
import kotlinx.android.synthetic.main.fragment_add_schedule.*
import java.util.*

class AddScheduleFragment : Fragment() {

    private lateinit var viewModel: MainActivityViewModel

    companion object {
        fun newInstance() = AddScheduleFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(MainActivityViewModel::class.java)
        calendarView.minDate = Calendar.getInstance().time.time
    }

}
