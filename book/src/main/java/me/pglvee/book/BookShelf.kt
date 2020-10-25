/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.book

import me.pglvee.database.entity.Book

data class BookShelf(
    val books: List<Book>,
    val page: Int = 0
) {
    fun add(books: List<Book>): BookShelf = BookShelf(this.books + books, page + 1)

}