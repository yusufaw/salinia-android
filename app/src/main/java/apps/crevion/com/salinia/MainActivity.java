package apps.crevion.com.salinia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import apps.crevion.com.salinia.model.User;
import apps.crevion.com.salinia.networking.RetrofitService;
import apps.crevion.com.salinia.ui.home.HomeActivity;
import apps.crevion.com.salinia.ui.note.NoteActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 1010;
    private static final String TAG = MainActivity.class.getName();
    GoogleSignInClient mGoogleSignInClient;

    TextView textView;
    SignInButton signInButton;
    Button buttonGoToLog;

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MainApp) getApplication()).getApplicationComponent().inject(this);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(getResources().getString(R.string.server_client_id))
                .requestScopes(new Scope(Scopes.PROFILE))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        textView = findViewById(R.id.textView);

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        buttonGoToLog = findViewById(R.id.go_to_log);
        buttonGoToLog.setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
        User user = new User("1", "Yusuf", "Aji Wibowo", "ucupper@gmail.com", "https://avatars2.githubusercontent.com/u/3977416?s=460&v=4");
        sharedPreferences.edit().putString("loggedInUser", new Gson().toJson(user)).apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);

            String authCode = account.getServerAuthCode();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("auth_code", authCode);

            RetrofitService.Creator.getInstance().userLogin(authCode)
                    .enqueue(new retrofit2.Callback<JsonObject>() {
                        @Override
                        public void onResponse(retrofit2.Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                            Log.d(TAG, "onResponse: " + response.body().toString());
                        }

                        @Override
                        public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {

                        }
                    });
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.toString());
            updateUI(null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateUI(GoogleSignInAccount googleSignInAccount) {
        if(googleSignInAccount != null) {
            textView.setText(googleSignInAccount.getDisplayName());
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.GONE);
        } else {
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
            textView.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
            case R.id.go_to_log:
                startActivity(new Intent(this, HomeActivity.class));
                break;
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }
}
