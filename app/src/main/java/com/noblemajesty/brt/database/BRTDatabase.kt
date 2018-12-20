package com.noblemajesty.brt.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.noblemajesty.brt.database.dao.BusDAO
import com.noblemajesty.brt.database.dao.BusScheduleDAO
import com.noblemajesty.brt.database.dao.UserDAO
import com.noblemajesty.brt.database.entities.Bus
import com.noblemajesty.brt.database.entities.BusSchedule
import com.noblemajesty.brt.database.entities.User

@Database(entities = [Bus::class, BusSchedule::class, User::class], version = 1, exportSchema = false)
abstract class BRTDatabase: RoomDatabase() {

    abstract fun userDAO(): UserDAO

    abstract fun busDAO(): BusDAO

    abstract fun busScheduleDAO(): BusScheduleDAO

    companion object {

        const val DATABASE_NAME = "brtDatabase.db"
        private var databaseInstance: BRTDatabase? = null

        fun getDatabaseInstance(context: Context): BRTDatabase {
            return databaseInstance ?: Room.databaseBuilder(context.applicationContext,
                    BRTDatabase::class.java, DATABASE_NAME).allowMainThreadQueries().build()
        }

        fun destroyInstance() {
            databaseInstance = null
        }

        fun populateBuses() {
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
            buses.map { databaseInstance?.busDAO()?.createBus(it) }
        }
    }
}