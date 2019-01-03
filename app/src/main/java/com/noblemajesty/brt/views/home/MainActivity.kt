package com.noblemajesty.brt.views.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.noblemajesty.brt.R
import com.noblemajesty.brt.Utils
import com.noblemajesty.brt.Utils.PREFERENCE
import com.noblemajesty.brt.Utils.userID
import com.noblemajesty.brt.database.BRTDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)

        drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(view: Int) { }
            override fun onDrawerSlide(view: View, p1: Float) { }
            override fun onDrawerClosed(view: View) { }
            override fun onDrawerOpened(view: View) { setDrawerInfo() }
        })

        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.database = BRTDatabase.getDatabaseInstance(this)
        viewModel.userId = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).getInt(userID, 0)

        goToFragment(RecentSchedulesFragment.newInstance(), null)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> { goToFragment(ProfileFragment.newInstance(), null) }
            R.id.recentSchedules -> { goToFragment(RecentSchedulesFragment.newInstance(), null) }
            R.id.addSchedule -> { goToFragment(AddScheduleFragment.newInstance(), null) }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun goToFragment(fragment: Fragment, bundle: Bundle?){
        bundle?.let { fragment.arguments = it }
        supportFragmentManager.beginTransaction()
                .replace(R.id.frameContainer, fragment)
                .addToBackStack(null)
                .commit()
    }
    private fun setDrawerInfo() {
        val preferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val firstName = preferences.getString(Utils.firstName, "")
        val lastName = preferences.getString(Utils.lastName, "")
        userName.text = "$firstName $lastName"
        userEmail.text = preferences.getString(Utils.email, "")
    }

    override fun onDestroy() {
        super.onDestroy()
        BRTDatabase.destroyInstance()
    }
}
