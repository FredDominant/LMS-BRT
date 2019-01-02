package com.noblemajesty.brt.views.home


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.noblemajesty.brt.R
import com.noblemajesty.brt.adapters.BusAdapter
import kotlinx.android.synthetic.main.fragment_available_buses.*

class AvailableBusesFragment : Fragment() {

    private var dayOfMonth: Int? = null
    private var month: Int? = null
    private var year: Int? = null

    private lateinit var viewModel: MainActivityViewModel

    companion object { fun newInstance() = AvailableBusesFragment() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_available_buses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(activity!!).get(MainActivityViewModel::class.java)
        val buses = viewModel.getAllBuses()
        activity?.actionBar?.title = "BUSES"
        Log.e("Busesssss", "$buses")
        buses?.let {
            noBusAvailableText.visibility = View.GONE
            availableBusesListView.visibility = View.VISIBLE
            availableText.visibility = View.VISIBLE

            val adapter = BusAdapter(it)
            val layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
            availableBusesListView.apply {
                this.layoutManager = layoutManager
                this.adapter = adapter
                setHasFixedSize(true)
            }
        }
    }

}
