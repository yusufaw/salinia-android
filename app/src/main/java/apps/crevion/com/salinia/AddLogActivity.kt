package apps.crevion.com.salinia

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import apps.crevion.com.salinia.MainActivity.JSON
import kotlinx.android.synthetic.main.activity_add_log.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class AddLogActivity : AppCompatActivity() {

    internal var okHttpClient = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_log)
        setSupportActionBar(toolbar)

        val textViewSave: TextView = findViewById(R.id.text_view_save)
        val editTextLog: EditText = findViewById(R.id.edit_text_log)
        var jsonObject = JSONObject()
        jsonObject.put("content", editTextLog.text)
        textViewSave.setOnClickListener {
            post("https://salinia-api.herokuapp.com/logs", jsonObject.toString(), object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    android.util.Log.d("xxx", "onFailure: " + e.toString())
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    finish()
                }
            })
        }
    }

    internal fun post(url: String, json: String, callback: Callback): Call {
        val body = RequestBody.create(JSON, json)
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
        val call = okHttpClient.newCall(request)
        call.enqueue(callback)
        return call
    }

}
