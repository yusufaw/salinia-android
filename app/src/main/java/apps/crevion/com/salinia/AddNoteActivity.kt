package apps.crevion.com.salinia

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import com.google.gson.JsonObject
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
                    .enqueue(object : retrofit2.Callback<JsonObject> {
                        override fun onResponse(call: retrofit2.Call<JsonObject>?, response: retrofit2.Response<JsonObject>?) {
                            finish()
                        }

                        override fun onFailure(call: retrofit2.Call<JsonObject>?, t: Throwable?) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    })
        }

        editTextNote.append((getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip.getItemAt(0).text)
    }
}
