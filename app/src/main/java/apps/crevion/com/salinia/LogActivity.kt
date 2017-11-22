package apps.crevion.com.salinia

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_log.*

class LogActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val recyclerLog: RecyclerView = findViewById(R.id.recycler_log)

        val logList: ArrayList<Log> = ArrayList()
        logList.add(Log(1, "test 1"))
        logList.add(Log(2, "test 2"))

        var adapter = LogAdapter(logList)
        recyclerLog.layoutManager = LinearLayoutManager(this)
        recyclerLog.adapter = adapter
    }
}
