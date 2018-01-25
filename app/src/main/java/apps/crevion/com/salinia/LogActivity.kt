package apps.crevion.com.salinia

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_log.*
import java.util.*

class LogActivity : AppCompatActivity() {

    var recyclerLog: RecyclerView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { startActivity(Intent(this, AddLogActivity::class.java)) }

        recyclerLog = findViewById(R.id.recycler_log)

        RetrofitService.Creator.getInstance().listLogs().enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(call: retrofit2.Call<JsonObject>, response: retrofit2.Response<JsonObject>) {
                val logList:List<Note> = Arrays.asList(*Gson().fromJson(response.body()!!.getAsJsonArray("data").toString(), Array<Note>::class.java))
                this@LogActivity.runOnUiThread({ updateAdapter(logList) })
            }

            override fun onFailure(call: retrofit2.Call<JsonObject>, t: Throwable) {
            }
        })
    }

    fun updateAdapter(noteList: List<Note>) {
        var adapter = LogAdapter(noteList)
        recyclerLog?.layoutManager = LinearLayoutManager(this)
        recyclerLog?.adapter = adapter
    }
}
