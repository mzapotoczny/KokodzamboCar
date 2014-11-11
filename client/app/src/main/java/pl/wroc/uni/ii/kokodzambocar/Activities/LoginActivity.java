package pl.wroc.uni.ii.kokodzambocar.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import pl.wroc.uni.ii.kokodzambocar.Commons;
import pl.wroc.uni.ii.kokodzambocar.R;

public class LoginActivity extends ActionBarActivity {
    static String ERROR_TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        try{
            Intent k = new Intent(LoginActivity.this, SessionStart.class);
            startActivity(k);
            finish();
        }catch (Exception e){
            Log.e(ERROR_TAG, "Could not start SessionStart activity");
        }
    }
}
