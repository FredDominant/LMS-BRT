package com.noblemajesty.brt.database.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "busSchedule")
data class BusSchedule (
        @PrimaryKey(autoGenerate = true)
        var scheduleId: Int?,
        var userId: Int,
        var busName: String,
        var destination: String,
        var from: String,
        var status: String,
        var date: String,
        var cost: String,
        var seatNumber: Int
)
{
    @Ignore
    constructor(): this(1, 0, "", "", "", "", "", "", 0)

    @Ignore
    constructor(userId: Int,
                busName: String,
                destination: String,
                from: String,
                status: String,
                date: String,
                cost: String,
                seatNumber: Int):
            this(1, userId, busName, destination, from, status, date, cost, seatNumber)
}