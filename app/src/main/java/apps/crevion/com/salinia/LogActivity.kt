package apps.crevion.com.salinia

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_log.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class LogActivity : AppCompatActivity() {


    internal var okHttpClient = OkHttpClient()
    var recyclerLog: RecyclerView?= null

    private val TAG = LogActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        recyclerLog = findViewById(R.id.recycler_log)
        val logList: ArrayList<Log> = ArrayList()

        get("http://192.168.1.32:3000/logs?page=1&limit=10", object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                android.util.Log.d(TAG, "onFailure: " + e.toString())
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val jsonObject1 = JSONObject(response.body()!!.string());
                val jsonArray = jsonObject1.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    val stringResponse = jsonArray.getJSONObject(i).toString();
                    logList.add(Gson().fromJson(stringResponse, Log::class.java))
                }
                this@LogActivity.runOnUiThread({ updateAdapter(logList) })
            }
        })
    }

    internal fun get(url: String, callback: Callback): Call {
        val request = Request.Builder()
                .url(url)
                .get()
                .build()
        val call = okHttpClient.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun updateAdapter(logList: ArrayList<Log>) {
        var adapter = LogAdapter(logList)
        recyclerLog?.layoutManager = LinearLayoutManager(this)
        recyclerLog?.adapter = adapter
    }
}
