package me.pglvee.demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.pglvee.book.BookFindActivity
import me.pglvee.book.TestActivity
import me.pglvee.media.VideoMediaActivity
import me.pglvee.network.doSuccess

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*viewModel.bookList.observe(this, Observer {
            it.doSuccess { Log.e("TAG", "success") }
        })
        viewModel.loadBookList()*/
//        startActivity(Intent(this, BookFindActivity::class.java))
//        startActivity(Intent(this, VideoMediaActivity::class.java))
        startActivity(Intent(this, TestActivity::class.java))
    }
}