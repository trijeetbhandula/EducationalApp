package com.example.educationalapp
//
//import android.content.Context
//import androidx.room.Room
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.educationalapp.database.AppDatabase
//import com.example.educationalapp.database.User
//import com.example.educationalapp.database.UserDao
//import kotlinx.coroutines.runBlocking
//import org.junit.After
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import kotlin.test.assertEquals
//import kotlin.test.assertNull
//
//@RunWith(AndroidJUnit4::class)
//class UserDaoTest {
//
//    private lateinit var db: AppDatabase
//    private lateinit var userDao: UserDao
//
//    @Before
//    fun createDb() {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
//        userDao = db.userDao()
//    }
//
//    @After
//    fun closeDb() {
//        db.close()
//    }
//
//    @Test
//    fun insertAndGetUser() = runBlocking {
//        val user = User(username = "testUser", score = 100)
//        userDao.insert(user)
//        val retrievedUser = userDao.getUser("testUser")
//        assertEquals(retrievedUser?.username, "testUser")
//        assertEquals(retrievedUser?.score, 100)
//    }
//
//    @Test
//    fun insertAndGetNonExistentUser() = runBlocking {
//        val retrievedUser = userDao.getUser("nonExistentUser")
//        assertNull(retrievedUser)
//    }
//
//    @Test
//    fun updateUserScore() = runBlocking {
//        val user = User(username = "testUser", score = 100)
//        userDao.insert(user)
//        val updatedUser = User(username = "testUser", score = 200)
//        userDao.insert(updatedUser)
//        val retrievedUser = userDao.getUser("testUser")
//        assertEquals(retrievedUser?.score, 200)
//    }
//}
