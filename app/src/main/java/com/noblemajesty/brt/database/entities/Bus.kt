package com.noblemajesty.brt.database.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "bus")
data class Bus (
        @PrimaryKey(autoGenerate = true)
        var busId: Int?,
        var capacity: Int,
        var name: String,
        var color: String
) {
    @Ignore
    constructor(): this(null, 0, "", "")

    @Ignore
    constructor(capacity: Int, name: String, color: String): this(null, capacity, name, color)
}