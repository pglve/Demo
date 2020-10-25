/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.pglvee.parse.ParseUtil

@ExperimentalCoroutinesApi
class BookViewModel : ViewModel() {

    private var _bookList = MutableStateFlow(BookShelf(emptyList(), 0))
    val bookList: StateFlow<BookShelf> get() = _bookList
    fun find() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = ParseUtil.find(_bookList.value.page)
            _bookList.value = _bookList.value.add(result)
        }
    }
}