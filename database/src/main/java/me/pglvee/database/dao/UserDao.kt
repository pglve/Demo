/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import me.pglvee.database.entity.User

/**
 * created by 2020/8/29
 * @author pinggonglve
 **/
@Dao
interface UserDao {

    @Query("select * from user")
    fun getAll(): LiveData<List<User>>

    @Query("select * from user where userId in (:userIds)")
    fun loadAllByIds(userIds: Array<String>): List<User>

    @Query("select * from user where nickName like :name limit 1")
    fun findByName(name: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)
}