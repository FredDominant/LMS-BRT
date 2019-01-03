package com.noblemajesty.brt.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.noblemajesty.brt.database.entities.BusSchedule

@Dao
interface BusScheduleDAO {

    @Query("SELECT * from busSchedule WHERE userId = :userId")
    fun getSchedules(userId: Int): List<BusSchedule>?

    @Insert
    fun addSchedule(busSchedule: BusSchedule): Long?

    @Update
    fun updateSchedule(busSchedule: BusSchedule)

    @Query("SELECT * from busSchedule WHERE scheduleId = :id")
    fun findScheduleById(id: Long): BusSchedule
}