package apps.crevion.com.salinia.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import apps.crevion.com.salinia.MainApp
import apps.crevion.com.salinia.R
import apps.crevion.com.salinia.model.Note
import apps.crevion.com.salinia.model.User
import apps.crevion.com.salinia.networking.RetrofitService
import apps.crevion.com.salinia.ui.note.NoteAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import java.util.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var recyclerNote: RecyclerView?= null

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        recyclerNote = findViewById(R.id.recycler_note)

        (application as MainApp).applicationComponent.inject(this)
        val user: User = Gson().fromJson(sharedPreferences.getString("loggedInUser", ""), User::class.java)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navigationView.getHeaderView(0)

        val userNameTextView: TextView = headerView.findViewById(R.id.user_name)
        val userEmailTextView = headerView.findViewById<TextView>(R.id.user_email)
        val userPhotoImageView = headerView.findViewById<ImageView>(R.id.user_photo)

        userNameTextView.text = user.firstName + " " + user.lastName
        userEmailTextView.text = user.email
        Glide.with(this).load(user.photoProfile).apply(RequestOptions.circleCropTransform()).into(userPhotoImageView)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
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
                .map { Arrays.asList(*Gson().fromJson(it.getAsJsonArray("data").toString(), Array<Note>::class.java)) }
                .subscribeWith(object: DisposableObserver<List<Note>>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: List<Note>) {
                        updateAdapter(t)
                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                    }

                })
    }
}
