package verbventures.frontend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import verbventures.frontend.ModelClasses.Admin;
import verbventures.frontend.ModelClasses.Verb;

/**
 * Created by Jacob on 11/8/2017.
 */

public class ManageVerbsActivity extends AppCompatActivity {

    private Admin admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_verbs);
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);

        final String TAG = "debug";

        final Context mcontext = this;
        admin = (Admin) getIntent().getSerializableExtra("admin");

        //grab the list view
        final ListView listView = (ListView) findViewById(R.id.verb_list);

        // Create the client and form the request
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://verb-ventures-api-dev.us-east-1.elasticbeanstalk.com/api/get-admin-verbs/" + admin.getAccountKitId())
                .build();

        // Call the client enqueue with a callback function
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"request unsuccessful");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //convert to a list from JSON using Gson
                Gson gson = new Gson();
                final Verb[] obtainedVerbs = gson.fromJson(response.body().string(), Verb[].class);

                //in order to populate the list, we need to call the main UI thread again
                ManageVerbsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        VerbArrayAdapter adapter = new VerbArrayAdapter(mcontext, obtainedVerbs);
                        // Attach the adapter to a ListView
                        listView.setAdapter(adapter);
                    }
                });

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_manageverbs:
                Intent manageVerbs = new Intent(this, ManageVerbsActivity.class);
                manageVerbs.putExtra("admin", admin);
                startActivity(manageVerbs);
                return true;

            case R.id.action_manageverbpacks:
                Intent manageVerbPacks = new Intent(this, ManageVerbPacksActivity.class);
                manageVerbPacks.putExtra("admin", admin);
                startActivity(manageVerbPacks);
                return true;

            case R.id.action_sessionreports:
                Intent sessionReports = new Intent(this, SessionReportsActivity.class);
                sessionReports.putExtra("admin", admin);
                startActivity(sessionReports);
                return true;

            case R.id.action_managestudents:
                Intent manageStudents = new Intent(this, ManageStudentsActivity.class);
                manageStudents.putExtra("admin", admin);
                startActivity(manageStudents);
                return true;

            case R.id.action_signout:
                Intent logout = new Intent(this, MainActivity.class);
                logout.putExtra("signout", true);
                startActivity(logout);
                return true;

            default:
                // if we get here, the user's action wasn't recognized
                // invoke superclass to handle it
                return super.onOptionsItemSelected(item);
        }
    }

    //on-Click methods
    public void onAddVerbClick(View v){
        Intent addVerb = new Intent(this, AddVerb.class);
        addVerb.putExtra("admin", admin);
        startActivity(addVerb);
    }
}
