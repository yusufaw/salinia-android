package apps.crevion.com.salinia

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_note.*
import java.util.*

class NoteActivity : AppCompatActivity() {

    var recyclerNote: RecyclerView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { startActivity(Intent(this, AddNoteActivity::class.java)) }
        recyclerNote = findViewById(R.id.recycler_note)
    }

    fun updateAdapter(noteList: List<Note>) {
        var adapter = NoteAdapter(noteList)
        recyclerNote?.layoutManager = LinearLayoutManager(this)
        recyclerNote?.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        RetrofitService.Creator.getInstance()
                .listLogs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ updateAdapter(Arrays.asList(*Gson().fromJson(it.getAsJsonArray("data").toString(), Array<Note>::class.java))) })
    }
}
