package apps.crevion.com.salinia

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import apps.crevion.com.salinia.model.User
import apps.crevion.com.salinia.module.PreferencesUtil
import apps.crevion.com.salinia.networking.RetrofitService
import apps.crevion.com.salinia.ui.home.HomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by yusufaw on 2/8/18.
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var preferencesUtil: PreferencesUtil

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var textView: TextView
    private lateinit var signInButton: SignInButton
    private lateinit var buttonGoToLog: Button

    private val RC_SIGN_IN = 1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        (application as MainApp).applicationComponent.inject(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(resources.getString(R.string.server_client_id))
                .requestScopes(Scope(Scopes.PROFILE))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        textView = findViewById(R.id.textView)

        signInButton = findViewById(R.id.sign_in_button)
        signInButton.setOnClickListener(this)
        buttonGoToLog = findViewById(R.id.go_to_log)
        buttonGoToLog.setOnClickListener(this)
        findViewById<View>(R.id.sign_out_button).setOnClickListener(this)
        findViewById<View>(R.id.disconnect_button).setOnClickListener(this)

        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)

    }

    fun updateUI(googleSignInAccount: GoogleSignInAccount?) {
        if (googleSignInAccount != null) {
            textView.text = googleSignInAccount.displayName
            findViewById<View>(R.id.sign_out_and_disconnect).visibility = View.VISIBLE
            signInButton.visibility = View.GONE
        } else {
            findViewById<View>(R.id.sign_out_and_disconnect).visibility = View.GONE
            signInButton.visibility = View.VISIBLE
            textView.text = ""
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.sign_in_button -> {
                val signInIntent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
            R.id.sign_out_button -> signOut()
            R.id.disconnect_button -> revokeAccess()
            R.id.go_to_log -> startActivity(Intent(this, HomeActivity::class.java))
        }
    }


    private fun signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this) { updateUI(null) }
    }

    private fun revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this) { updateUI(null) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account)

            val authCode = account.serverAuthCode
            val jsonObject = JSONObject()
            jsonObject.put("auth_code", authCode)
            RetrofitService.Creator.getInstance().userLogin(authCode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { Gson().fromJson(it.getAsJsonObject("data").toString(), User::class.java) }
                    .subscribeWith(object: DisposableObserver<User>() {
                        override fun onComplete() {

                        }

                        override fun onNext(t: User) {
                            preferencesUtil.putUserLogin(t)
                        }

                        override fun onError(e: Throwable) {
                            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                        }

                    })
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("xxx", "signInResult:failed code=" + e.toString())
            updateUI(null)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }
}