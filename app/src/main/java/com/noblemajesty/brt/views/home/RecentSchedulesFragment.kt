package com.noblemajesty.brt.views.home

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.noblemajesty.brt.R
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
            if (it.isEmpty()) { recentScheduleListView.visibility = View.GONE }
            else { Log.e("Recent Schedules", "${it.size}") }
        }
    }

}
