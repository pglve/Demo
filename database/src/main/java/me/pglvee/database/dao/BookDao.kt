/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.database.dao

import androidx.room.Dao
import androidx.room.Insert
import me.pglvee.database.AppDatabase
import me.pglvee.database.entity.Book

/**
 * created by 2020/10/25
 * @author pinggonglve
 **/
@Dao
interface BookDao {

    @Insert
    suspend fun insertAll(book: List<Book>)
}
