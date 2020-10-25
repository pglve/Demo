/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.database.repository

import androidx.lifecycle.LiveData
import me.pglvee.database.dao.UserDao
import me.pglvee.database.entity.User

/**
 * created by 2020/8/30
 * @author pinggonglve
 **/
class UserRepository(private val userDao: UserDao) {

    val allUsers: LiveData<List<User>> = userDao.getAll()

    suspend fun insert(user: User) {
        userDao.insert(user)
    }
}