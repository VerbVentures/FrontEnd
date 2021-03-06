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
import verbventures.frontend.ModelClasses.Admin;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import verbventures.frontend.ModelClasses.Verb;
import verbventures.frontend.ModelClasses.VerbPack;

/**
 * Created by Jacob on 11/8/2017.
 */

public class ManageVerbPacksActivity extends AppCompatActivity {


    private final String TAG = "ManageVerbPacks";
    private Admin admin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_verb_packs);
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);
        setTitle("Manage Verb Packs");


        final Context mcontext = this;
        final ListView listView = findViewById(R.id.verbPack_list);
        admin = (Admin) getIntent().getSerializableExtra("admin");


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://verb-ventures-api-dev.us-east-1.elasticbeanstalk.com/api/verbpacks/")
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
                final VerbPack[] obtainedVerbPacks = gson.fromJson(response.body().string(), VerbPack[].class);

                for (int i = 0; i < obtainedVerbPacks.length; ++i) {
                    obtainedVerbPacks[i].setAdminObj(admin);
                }

                //in order to populate the list, we need to call the main UI thread again
                ManageVerbPacksActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        VerbPackArrayAdapter adapter = new VerbPackArrayAdapter(mcontext, obtainedVerbPacks);
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



    public void onCreateVerbPackClick(View v){
        Intent intent = new Intent(ManageVerbPacksActivity.this, createVerbPack.class);
        intent.putExtra("admin", admin);
        startActivity(intent);
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
}
