package pl.wroc.uni.ii.kokodzambocar;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class LoginActivity extends ActionBarActivity {
    static String kErrorTag = "KoCar";

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
            Log.e(kErrorTag, "Could not start SessionStart activity");
        }
    }
}
