package apps.crevion.com.salinia

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_note.*
import org.json.JSONObject

class AddNoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        setSupportActionBar(toolbar)

        val textViewSave: TextView = findViewById(R.id.text_view_save)
        val editTextNote: EditText = findViewById(R.id.edit_text_note)
        var jsonObject = JSONObject()
        jsonObject.put("content", editTextNote.text)
        textViewSave.setOnClickListener {
            RetrofitService.Creator.getInstance().addNote(editTextNote.text.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { jsonObject -> Gson().fromJson(jsonObject.getAsJsonObject("data"), Note::class.java) }
                    .subscribeWith(object: DisposableObserver<Note>() {
                        override fun onComplete() {
                            finish()
                        }

                        override fun onNext(t: Note) {
                        }

                        override fun onError(e: Throwable) {
                        }
                    })
        }

        editTextNote.append((getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip.getItemAt(0).text)
    }
}
