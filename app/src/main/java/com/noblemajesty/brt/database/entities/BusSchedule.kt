package com.noblemajesty.brt.database.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "busSchedule")
data class BusSchedule (
        @PrimaryKey(autoGenerate = true)
        var scheduleId: Int?,
        var userId: Int,
        var busId: Int,
        var busName: String,
        var destination: String,
        var departure: Long,
        var status: String
)
{
    @Ignore
    constructor(): this(0, 0, 0, "", "", 0, "")

    @Ignore
    constructor(userId: Int, busId: Int, busName: String, destination: String, departure: Long, status: String): this(null, userId, busId, busName, destination, departure, status)
}