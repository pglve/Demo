/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.database

import me.pglvee.database.dao.BookDao

/**
 * created by 2020/10/25
 * @author pinggonglve
 **/
object DBUtil {

    fun getBookDao(): BookDao = AppDatabase.getDatabase().bookDao()
}