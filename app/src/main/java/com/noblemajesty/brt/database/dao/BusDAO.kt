package com.noblemajesty.brt.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.noblemajesty.brt.database.entities.Bus

@Dao
interface BusDAO {

    @Query("SELECT * FROM bus WHERE busId = :busId")
    fun findBusById(busId: Int): Bus

    @Query("SELECT * FROM bus")
    fun getAllBuses(): List<Bus>?

    @Insert
    fun createBus(bus: Bus)

    @Update
    fun updateBus(bus: Bus)
}