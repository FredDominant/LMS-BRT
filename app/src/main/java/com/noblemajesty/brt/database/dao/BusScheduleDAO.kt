package com.noblemajesty.brt.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.noblemajesty.brt.database.entities.BusSchedule

@Dao
interface BusScheduleDAO {

    @Query("SELECT * FROM busSchedule")
    fun getAllUserSchedule(): List<BusSchedule>

    @Insert
    fun addSchedule(busSchedule: BusSchedule)

    @Update
    fun updateSchedule(busSchedule: BusSchedule)
}