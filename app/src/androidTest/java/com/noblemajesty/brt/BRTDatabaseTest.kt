package com.noblemajesty.brt

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.noblemajesty.brt.R.id.bus
import com.noblemajesty.brt.database.BRTDatabase
import com.noblemajesty.brt.database.dao.BusDAO
import com.noblemajesty.brt.database.dao.BusScheduleDAO
import com.noblemajesty.brt.database.dao.UserDAO
import com.noblemajesty.brt.database.entities.Bus
import com.noblemajesty.brt.database.entities.User
import junit.framework.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BRTDatabaseTest {

    private lateinit var userDao: UserDAO
    private lateinit var busDAO: BusDAO
    private lateinit var busScheduleDAO: BusScheduleDAO
    private lateinit var db: BRTDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getContext()
        db = Room.inMemoryDatabaseBuilder(
                context, BRTDatabase::class.java).build()
        userDao = db.userDAO()
        busDAO = db.busDAO()
        busScheduleDAO = db.busScheduleDAO()
    }

    private fun createTestUser(): User {
        return User().apply {
            firstName = "firstName"
            lastName = "lastName"
            email = "email"
            password = "password"
        }
    }

    private fun createTestBus(): Bus {
        return Bus().apply {
            capacity = 10
            name = "Bus One"
            color = "Blue"
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun database_shouldCreateUsersSuccessfully() {
        val user = createTestUser()
        assertEquals("email", user.email)
        assertEquals("lastName", user.lastName)
        val userId = userDao.createUser(user)
        assertNotNull(userId)
    }

    @Test
    fun database_shouldFetchUsersSuccessfully() {
        val user = createTestUser()
        assertEquals("email", user.email)
        assertEquals("lastName", user.lastName)
        val fetchedUser = userDao.findUserByEmail(user.email)
        assertNull(fetchedUser)
        userDao.createUser(user)
        val registeredUser = userDao.findUserByEmail(user.email)
        assertEquals(user.email, registeredUser?.email)
        assertEquals(user.lastName, registeredUser?.lastName)
        assertEquals(user.firstName, registeredUser?.firstName)
        assertEquals(user.email, registeredUser?.email)
        assertEquals(1, registeredUser?.userId)
    }

    @Test
    fun database_shouldUpdateUser() {
        val testUser = createTestUser()
        userDao.createUser(testUser)
        val user = userDao.findUserByEmail(testUser.email)
        assertNotNull(user)
        assertEquals(user?.email, testUser.email)
        user!!.apply {
            firstName = "updatedFirstName"
            email = "updatedEmail"
        }
        val updatedRows = userDao.updateUser(user)
        assertNotNull(updatedRows)
        val updatedUser = userDao.findUserByEmail(user.email)
        assertNotNull(updatedUser)
        assertEquals(updatedUser?.email, user.email)
    }

    @Test
    fun database_shouldReturnCurrentUser() {
        val testUser = createTestUser()
        userDao.createUser(testUser)
        val currentUser = userDao.getCurrentUser(1)
        assertNotNull(currentUser)
        assertEquals(currentUser?.email, testUser.email)
        assertEquals(currentUser?.userId, 1)
        assertEquals(currentUser?.lastName, testUser.lastName)
    }

    @Test
    fun database_ShouldCreateBus() {
        val testBus = createTestBus()
        val busId = busDAO.createBus(testBus)
        assertNotNull(busId)
        assertEquals(busId, 1.toLong())
    }
}