/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.pglvee.database.DBUtil
import me.pglvee.database.entity.Book
import me.pglvee.network.Response
import me.pglvee.parse.ParseUtil

/**
 * created by 2020/8/30
 * @author pinggonglve
 **/
class MainViewModel: ViewModel() {

    private var _bookList = MutableLiveData<Response<List<Book>?>>()
    val bookList: LiveData<Response<List<Book>?>> = _bookList
    fun loadBookList() {
        val dao = DBUtil.getBookDao()
        viewModelScope.launch(Dispatchers.IO) {
//            val result = requestApi { MainApiService.find() }
            val result = ParseUtil.find(1)
            dao.insertAll(result)
            _bookList.postValue(Response(result))
        }
    }
}