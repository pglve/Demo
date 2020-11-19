/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.book

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.onActive
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.pglvee.base.density
import me.pglvee.base.screenWidth
import me.pglvee.database.entity.Book

class BookFindActivity : AppCompatActivity() {
    @ExperimentalCoroutinesApi
    private val viewModel by viewModels<BookViewModel>()

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookShelfPage(viewModel) {
                Log.e("书名", it.bookName ?: "")
            }
        }
        viewModel.find()
    }
}

const val rowSize = 3

val itemWidth: Dp by lazy { Dp(screenWidth / density).minus(40.dp).div(3) }
val imageWidth: Dp by lazy { itemWidth.minus(10.dp) }
val imageHeight: Dp by lazy { imageWidth.div(0.618f) }

@ExperimentalCoroutinesApi
@Composable
fun BookShelfPage(viewModel: BookViewModel, onItemClick: (Book) -> Unit) {
    val state = viewModel.bookList.collectAsState()
    val itemsWind = state.value.books.windowed(rowSize, rowSize, true)
    val lastIndex = itemsWind.lastIndex
    LazyColumnForIndexed(itemsWind) { index, items ->
        if (lastIndex == index) {
            onActive(callback = {
                viewModel.find()
            })
        }
        Row {
            Spacer(modifier = Modifier.preferredSize(10.dp))
            for (item in items) {
                BookShelf(item, onItemClick)
                Spacer(modifier = Modifier.preferredSize(10.dp))
            }
        }
    }
}

@Composable
fun BookShelf(book: Book, onItemClick: (Book) -> Unit) {
    Column(
        modifier = Modifier.preferredWidth(itemWidth)
            .clickable(onClick = { onItemClick(book) })
    ) {
        CoilImage(
            book.pictureUrl ?: "",
            modifier = Modifier.preferredSize(imageWidth, imageHeight)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            book.bookName ?: "", fontSize = 16.sp,
            modifier = Modifier.preferredWidth(imageWidth)
        )
        Spacer(modifier = Modifier.preferredSize(5.dp))
    }
}