package pl.wroc.uni.ii.kokodzambocar.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pl.wroc.uni.ii.kokodzambocar.Api.Api;
import pl.wroc.uni.ii.kokodzambocar.Api.Measurement;
import pl.wroc.uni.ii.kokodzambocar.Api.Session;
import pl.wroc.uni.ii.kokodzambocar.Api.SessionApi;
import pl.wroc.uni.ii.kokodzambocar.Api.SessionName;
import pl.wroc.uni.ii.kokodzambocar.Constants;
import pl.wroc.uni.ii.kokodzambocar.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SessionStart extends ActionBarActivity {
    private static String ERROR_TAG = "SessionStart";
    private ProgressDialog mGetProgressDialog;
    private List<Session> mSessions;
    private ListView mSessionsListView;
    private ArrayAdapter<String> mSessionsListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_start);
        mSessionsListView = (ListView) findViewById(R.id.sessionsListView);
        mSessionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToSession(position);
            }
        });

        getSessions();
    }

    private void getSessions() {
        mGetProgressDialog = ProgressDialog.show(this, "Sessions", "Downloading sessions from server");
        Api.getInstance().session.getSessions(new Callback<List<Session>>() {
            @Override
            public void success(List<Session> sessions, Response response) {
                mGetProgressDialog.dismiss();
                setSessions(sessions);
            }

            @Override
            public void failure(RetrofitError error) {
                mGetProgressDialog.dismiss();
                new AlertDialog.Builder(SessionStart.this).
                        setTitle("Error").
                        setMessage("An error occurred when receiving session list. Reason: "+error.getCause().toString()).
                        setNeutralButton("OK", null).
                        show();
            }
        });
    }

    protected void setSessions(List<Session> sessions){
        mSessions = sessions;
        ArrayList<String> strSessions = new ArrayList<String>();
        for (Session s : sessions){
            strSessions.add(s.name);
        }

        mSessionsListViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, strSessions);
        mSessionsListView.setAdapter(mSessionsListViewAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_session_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh_sessions:
                getSessions();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void newSessionButtonClick(View view) {
        final EditText input = new EditText(this);
        input.setTextColor(Color.WHITE);
        new AlertDialog.Builder(this).
                setTitle("Session name").
                setMessage("Set name for new session").
                setView(input).
                setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addSession(input.getText().toString());
                    }
                }).
                setNegativeButton("Cancel", null).
                show();
    }

    private void goToSession(Integer sessionId) {
        try{
            Session session = mSessions.get(sessionId);
            Intent k = new Intent(SessionStart.this, Status.class);
            k.putExtra(Constants.INTENT_SESSION_ID, session.id);
            k.putExtra(Constants.INTENT_SESSION_NAME, session.name);
            startActivity(k);
        }catch (Exception e){
            Log.e(ERROR_TAG, "Could not start SessionStart activity");
        }
    }

    public void addSession(String name) {
        Api.getInstance().session.addSession(new SessionName(name), new Callback<Session>() {
            @Override
            public void success(Session session, Response response) {
                mSessions.add(session);
                mSessionsListViewAdapter.add(session.name);
                mSessionsListViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                new AlertDialog.Builder(SessionStart.this).
                        setTitle("Error").
                        setMessage("An error occurred when adding session. Reason: "+error.getCause().toString()).
                        setNeutralButton("OK", null).
                        show();
            }
        });
    }
}
