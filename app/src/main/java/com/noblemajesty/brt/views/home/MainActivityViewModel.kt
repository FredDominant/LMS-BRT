package com.noblemajesty.brt.views.home

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.noblemajesty.brt.database.BRTDatabase
import com.noblemajesty.brt.database.entities.BusSchedule
import com.noblemajesty.brt.database.entities.User

class MainActivityViewModel : ViewModel() {
    var database: BRTDatabase? = null
    var userId: Int? = null

    fun getSchedules(): List<BusSchedule>? {
        return database?.busScheduleDAO()?.getAllUserSchedule()
    }

    fun addSchedule(busSchedule: BusSchedule): Unit? {
        return database?.busScheduleDAO()?.addSchedule(busSchedule)
    }

    fun getUser(): User? {
        return database?.userDAO()?.getCurrentUser(userId!!)
    }

    fun updateProfile(user: User) {
        database?.userDAO()?.updateUser(user)
    }

}
