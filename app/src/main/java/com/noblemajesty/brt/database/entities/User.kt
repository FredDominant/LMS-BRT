package com.noblemajesty.brt.database.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "user")
data class User (
        @PrimaryKey(autoGenerate = true)
        var userId: Int?,
        var firstName: String = "",
        var lastName: String = "",
        var email: String = "",
        var password: String = ""
)
{
    @Ignore
    constructor(): this(1, "", "", "", "")

    @Ignore
    constructor(firstName: String,
                lastName: String,
                email: String,
                password: String): this(1, firstName, lastName, email, password)
}