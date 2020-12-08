package me.pglvee.demo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import me.pglvee.base.preferencesData
import me.pglvee.base.read
import me.pglvee.base.write

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private val storeData by lazy { this.preferencesData }

    private var counter: Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*viewModel.bookList.observe(this, Observer {
            it.doSuccess { Log.e("TAG", "success") }
        })
        viewModel.loadBookList()*/
//        startActivity(Intent(this, BookFindActivity::class.java))
//        startActivity(Intent(this, VideoMediaActivity::class.java))

        findViewById<View>(R.id.writeBtn).setOnClickListener {
            lifecycleScope.launch {
                storeData.write("name", counter ++)
            }
        }

        val register = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            Log.e("uri", "uri : $it")
        }
        findViewById<View>(R.id.readBtn).setOnClickListener {
            /*lifecycleScope.launch {
                val counter = storeData.read<Int>("name")
                findViewById<TextView>(R.id.result).text = counter.toString()
            }*/


            register.launch(arrayOf("image/*"))
        }

    }
}