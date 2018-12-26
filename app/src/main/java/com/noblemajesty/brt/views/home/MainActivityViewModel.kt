package com.noblemajesty.brt.views.home

import android.arch.lifecycle.ViewModel
import com.noblemajesty.brt.database.BRTDatabase
import com.noblemajesty.brt.database.entities.Bus
import com.noblemajesty.brt.database.entities.BusSchedule
import com.noblemajesty.brt.database.entities.User

class MainActivityViewModel : ViewModel() {
    var database: BRTDatabase? = null
    var userId: Int? = null
    var year: Int? = null
    var month: Int? = null
    var day: Int? = null
    var hour: Int? = null
    var minute: Int? = null

    fun getSchedules() = database?.busScheduleDAO()?.getAllUserSchedule()

    fun addSchedule(busSchedule: BusSchedule) = database?.busScheduleDAO()?.addSchedule(busSchedule)

    fun getUser() = database?.userDAO()?.getCurrentUser(userId!!)

    fun updateProfile(user: User) = database?.userDAO()?.updateUser(user)

    fun getAllBuses(): List<Bus>? {
        var buses = database?.busDAO()?.getAllBuses()
        if (buses?.isEmpty()!!) database?.populateBuses()
        buses = database?.busDAO()?.getAllBuses()
        return buses
    }
}
