/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.pglvee.base.BaseApp
import me.pglvee.database.dao.BookDao
import me.pglvee.database.dao.UserDao
import me.pglvee.database.entity.Book
import me.pglvee.database.entity.User

/**
 * created by 2020/8/29
 * @author pinggonglve
 **/
@Database(
    entities = [User::class, Book::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun bookDao(): BookDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room
                    .databaseBuilder(
                        BaseApp.instance.applicationContext,
                        AppDatabase::class.java,
                        "demo.db"
                    )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
