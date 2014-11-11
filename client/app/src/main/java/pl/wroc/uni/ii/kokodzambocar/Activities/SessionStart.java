package pl.wroc.uni.ii.kokodzambocar.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import pl.wroc.uni.ii.kokodzambocar.R;


public class SessionStart extends ActionBarActivity {
    private static String ERROR_TAG = "SessionStart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_start);

        ArrayList<String> sessions = new ArrayList<String>();
        sessions.add("First");
        sessions.add("Second");
        sessions.add("Third");

        final ListView view = (ListView) findViewById(R.id.sessionsListView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, sessions);
        view.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_session_start, menu);
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

    public void newSessionButtonClick(View view) {
        try{
            Intent k = new Intent(SessionStart.this, Status.class);
            startActivity(k);
        }catch (Exception e){
            Log.e(ERROR_TAG, "Could not start SessionStart activity");
        }
    }
}
