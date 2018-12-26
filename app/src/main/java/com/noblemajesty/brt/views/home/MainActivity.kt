package com.noblemajesty.brt.views.home

import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.noblemajesty.brt.R
import com.noblemajesty.brt.Utils
import com.noblemajesty.brt.Utils.PREFERENCE
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)

        drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(p0: Int) { }
            override fun onDrawerSlide(p0: View, p1: Float) { }
            override fun onDrawerClosed(p0: View) { }
            override fun onDrawerOpened(p0: View) { setDrawerInfo() }
        })

        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.profile -> { }
            R.id.recentSchedules -> { }
            R.id.addSchedule -> { }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setDrawerInfo() {
        val preferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val firstName = preferences.getString(Utils.firstName, "")
        val lastName = preferences.getString(Utils.lastName, "")
        userName.text = "$firstName $lastName"
        userEmail.text = preferences.getString(Utils.email, "")
    }
}
