package com.noblemajesty.brt.views.home

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.noblemajesty.brt.R
import com.noblemajesty.brt.adapters.BusScheduleAdapter
import com.noblemajesty.brt.database.entities.BusSchedule
import kotlinx.android.synthetic.main.recent_schedules_fragment.*

class RecentSchedulesFragment : Fragment() {

    companion object {
        fun newInstance() = RecentSchedulesFragment()
    }

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        return inflater.inflate(R.layout.recent_schedules_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val schedules = viewModel.getSchedules()
        schedules?.let {
            val adapter = BusScheduleAdapter(it as ArrayList<BusSchedule>)
            val layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
            recentScheduleListView.apply {
                this.adapter = adapter
                this.layoutManager = layoutManager
                setHasFixedSize(true)
                noRecentScheduleText.text = if (it.isEmpty()) "You've not taken any trip" else "Your recent trips"
                visibility = View.VISIBLE
            }
        }
    }

}
