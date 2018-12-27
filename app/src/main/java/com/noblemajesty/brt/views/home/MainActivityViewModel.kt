package com.noblemajesty.brt.views.home

import android.arch.lifecycle.ViewModel
import android.util.Log
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
        if (buses?.isEmpty()!!) populateBuses()
        buses = database?.busDAO()?.getAllBuses()
        return buses
    }

    private fun populateBuses() {
        val buses = listOf(
                Bus(capacity = 18, name = "Hiace", color = "brown"),
                Bus(capacity = 12, name = "Karosa", color = "white"),
                Bus(capacity = 16, name = "Fiat", color = "black"),
                Bus(capacity = 14, name = "Volkswagen", color = "yellow"),
                Bus(capacity = 10, name = "Wrightbus", color = "white"),
                Bus(capacity = 8, name = "Volvo", color = "red"),
                Bus(capacity = 8, name = "Kia", color = "white"),
                Bus(capacity = 10, name = "Hyundai", color = "blue"),
                Bus(capacity = 14, name = "Irizar", color = "white"),
                Bus(capacity = 14, name = "Nissan", color = "grey")
        )
        buses.map { database?.busDAO()?.createBus(it) }
    }
}
