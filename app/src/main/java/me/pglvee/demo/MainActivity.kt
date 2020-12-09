package me.pglvee.demo

import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.ImageView
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
import me.pglvee.compress.CompressUtil
import me.pglvee.compress.CompressUtil.compress
import java.io.*

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
                storeData.write("name", counter++)
            }
        }

        val register = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            Log.e("compress", "uri : $it")
            dumpImageMetaData(it)
            val bitmap = getBitmapFromUri(it)
            Log.e("compress", "compress before")
            val file = File.createTempFile("temp_s", ".jpg", cacheDir)
            CompressUtil.newInstance(this)
                .src(bitmap)
                .dst(file)
                .dimension(480, 480)
                .maxSize(20 * 1024)
                .compress()
            Log.e("compress", "compress after size : ${file.length()}")
            findViewById<ImageView>(R.id.testIv).setImageURI(Uri.fromFile(file))

        }
        findViewById<View>(R.id.readBtn).setOnClickListener {
            /*lifecycleScope.launch {
                val counter = storeData.read<Int>("name")
                findViewById<TextView>(R.id.result).text = counter.toString()
            }*/

            register.launch(arrayOf("image/*"))
        }

    }

    fun dumpImageMetaData(uri: Uri) {

        // The query, because it only applies to a single document, returns only
        // one row. There's no need to filter, sort, or select fields,
        // because we want all fields for one document.
        val cursor: Cursor? = contentResolver.query(
            uri, null, null, null, null, null
        )

        cursor?.use {
            // moveToFirst() returns false if the cursor has 0 rows. Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (it.moveToFirst()) {

                // Note it's called "Display Name". This is
                // provider-specific, and might not necessarily be the file name.
                val displayName: String =
                    it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                Log.i("compress", "Display Name: $displayName")

                val sizeIndex: Int = it.getColumnIndex(OpenableColumns.SIZE)
                // If the size is unknown, the value stored is null. But because an
                // int can't be null, the behavior is implementation-specific,
                // and unpredictable. So as
                // a rule, check if it's null before assigning to an int. This will
                // happen often: The storage API allows for remote files, whose
                // size might not be locally known.
                val size: String = if (!it.isNull(sizeIndex)) {
                    // Technically the column stores an int, but cursor.getString()
                    // will do the conversion automatically.
                    it.getString(sizeIndex)
                } else {
                    "Unknown"
                }
                Log.i("compress", "Size: $size")
            }
        }
    }

    @Throws(IOException::class)
    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val parcelFileDescriptor: ParcelFileDescriptor? =
            contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor: FileDescriptor? = parcelFileDescriptor?.fileDescriptor
        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor?.close()
        return image
    }

    @Throws(IOException::class)
    private fun readTextFromUri(uri: Uri): String {
        val stringBuilder = StringBuilder()
        contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line: String? = reader.readLine()
                while (line != null) {
                    stringBuilder.append(line)
                    line = reader.readLine()
                }
            }
        }
        return stringBuilder.toString()
    }

    private fun alterDocument(uri: Uri) {
        try {
            contentResolver.openFileDescriptor(uri, "w")?.use {
                FileOutputStream(it.fileDescriptor).use {
                    it.write(
                        ("Overwritten at ${System.currentTimeMillis()}\n")
                            .toByteArray()
                    )
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun deleteDocument(uri: Uri) {
        DocumentsContract.deleteDocument(applicationContext.contentResolver, uri)
    }
}