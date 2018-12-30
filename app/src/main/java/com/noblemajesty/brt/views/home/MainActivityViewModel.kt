package com.noblemajesty.brt.views.home

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.noblemajesty.brt.BusSeat
import com.noblemajesty.brt.database.BRTDatabase
import com.noblemajesty.brt.database.entities.Bus
import com.noblemajesty.brt.database.entities.BusSchedule
import com.noblemajesty.brt.database.entities.User
import java.util.*
import kotlin.collections.ArrayList

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    var database: BRTDatabase? = null
    var userId: Int? = null
    var year: Int? = null
    var month: Int? = null
    var day: Int? = null
    var hour: Int? = null
    var minute: Int? = null
    var departure: String? = null
    var destination: String? = null
    var busName: String? = null
    var scheduleId: Long? = null

    init {
        database = BRTDatabase.getDatabaseInstance(getApplication())
    }

    fun getSchedules(): List<BusSchedule>? {
        val schedulesList = ArrayList<BusSchedule>()
        Log.e("DB Instance newwwww", "$database")
//        for (scheduleId in 1..10) {
//            val schedule = database?.busScheduleDAO()?.findScheduleById(scheduleId.toLong())
//            schedule?.let { schedulesList.add(it)}
//        }
        return database?.busScheduleDAO()?.getSchedules()
    }

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
                Bus(capacity = 12, name = "Van Hool Sleeper Coach", color = "Brown"),
                Bus(capacity = 12, name = "Neoplan Jumbocruiser", color = "White"),
                Bus(capacity = 16, name = "Hino S'elega", color = "Black"),
                Bus(capacity = 10, name = "Greyhound Lines", color = "Black"),
                Bus(capacity = 10, name = "InterUrbino 12", color = "White"),
                Bus(capacity = 10, name = "Victory Liner", color = "Red"),
                Bus(capacity = 12, name = "Volzhanin-5285", color = "White"),
                Bus(capacity = 16, name = "PD-4501 Scenicruiser", color = "Blue"),
                Bus(capacity = 14, name = "Eclipse Fusion", color = "White"),
                Bus(capacity = 14, name = "FX212 Super Cruiser", color = "Grey")
        )
        buses.map { database?.busDAO()?.createBus(it) }
    }

    fun populateBusSeats(numberOfSeats: Int): ArrayList<BusSeat> {
        val busSeats = ArrayList<BusSeat>()
        for (seat in 1..numberOfSeats) {
            val busSeat = BusSeat().apply {
                number = seat
                isAvailable = generateRandomNumber() % 2 == 0
            }
            busSeats.add(busSeat)
        }
        return busSeats
    }

    private fun generateRandomNumber() = Random().nextInt()

    fun addBusSchedule(busSchedule: BusSchedule): Long? {
        scheduleId = database?.busScheduleDAO()?.addSchedule(busSchedule)
        Log.e("Saved schedule ID", "$scheduleId")
        Log.e("getting all", "${getBusSchedule(1)}")
        return scheduleId
    }

    fun getBusSchedule(id: Long): BusSchedule? {
        return database?.busScheduleDAO()?.findScheduleById(id)
    }

    fun updateSchedule(busSchedule: BusSchedule) {
        database?.busScheduleDAO()?.updateSchedule(busSchedule)
    }
}
