/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.database.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.pglvee.database.AppDatabase
import me.pglvee.database.entity.User
import me.pglvee.database.repository.UserRepository

/**
 * created by 2020/8/30
 * @author pinggonglve
 **/
class UserViewModel(application: Application): AndroidViewModel(application) {

    private val repository: UserRepository

    val allUsers: LiveData<List<User>>

    init {
        val userDao = AppDatabase.getDatabase().userDao()
        val bookDao = AppDatabase.getDatabase().bookDao()
        repository = UserRepository(userDao)
        allUsers = repository.allUsers
    }

    fun insert(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(user)
    }
}