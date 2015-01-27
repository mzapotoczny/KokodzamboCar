package pl.wroc.uni.ii.kokodzambocar.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import pl.wroc.uni.ii.kokodzambocar.Api.Api;
import pl.wroc.uni.ii.kokodzambocar.Api.Authentication;
import pl.wroc.uni.ii.kokodzambocar.Api.Token;
import pl.wroc.uni.ii.kokodzambocar.Commons;
import pl.wroc.uni.ii.kokodzambocar.R;
import pl.wroc.uni.ii.kokodzambocar.Services.Uploader;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends ActionBarActivity {
    static String ERROR_TAG = "LoginActivity";

    private EditText mLoginText;
    private EditText mPasswordText;
    private ProgressDialog mLoginProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginText = (EditText) findViewById(R.id.loginText);
        mPasswordText = (EditText) findViewById(R.id.passwordText);
        Intent uploaderIntent = new Intent(this, Uploader.class);
        this.stopService(uploaderIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void registerLabelClick(View view) {
        Commons.showNotImplemented(this);
    }

    public void loginButtonClick(View view) {
        mLoginProgressDialog = ProgressDialog.show(this, "Please wait", "Logging in");
        Authentication aut = new Authentication();
        aut.authenticate(mLoginText.getText().toString(),
                mPasswordText.getText().toString(),
                new Callback<Token>() {
                    @Override
                    public void success(Token token, Response response) {
                        mLoginProgressDialog.dismiss();
                        Api.token = token;
                        loggedInAction();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mLoginProgressDialog.dismiss();
                        Commons.showMessage(LoginActivity.this, "Error", "Wrong credentials");
                    }
                }
                );
    }

    private void loggedInAction() {
        try{
            Intent uploaderIntent = new Intent(this, Uploader.class);
            this.startService(uploaderIntent);

            Intent k = new Intent(this, SessionStart.class);
            startActivity(k);
            finish();
        }catch (Exception e){
            Log.e(ERROR_TAG, "Could not start SessionStart activity");
        }
    }
}
