/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * created by 2020/10/25
 * @author pinggonglve
 **/

@Entity
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    var pictureUrl: String?,
    var bookName: String?,
    var author: String?,
    var bookUrl: String?,
    var latestDate: String?,
    var latestChapterUrl: String?,
    var latestChapterName: String?,
    var description: String?
)