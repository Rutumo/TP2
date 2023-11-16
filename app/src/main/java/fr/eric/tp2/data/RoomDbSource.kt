package fr.eric.tp2.data

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.eric.tp2.Model.User
import fr.eric.tp2.Model.UserDao

@Database(entities = [User::class], version = 1)
abstract class RoomDbSource : RoomDatabase()  {
    abstract fun userDao(): UserDao
}