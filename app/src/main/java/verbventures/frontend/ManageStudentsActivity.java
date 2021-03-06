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
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import verbventures.frontend.ModelClasses.Admin;
import verbventures.frontend.ModelClasses.Student;
import verbventures.frontend.ModelClasses.Verb;

public class ManageStudentsActivity extends AppCompatActivity {

    private ListView studentList;
    private StudentArrayAdapter adapter;
    private Admin admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);
        setTitle("Manage Students");

        final Context mcontext = this;
        admin = (Admin) getIntent().getSerializableExtra("admin");

        final String TAG = "debug";

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Grab the list view
        this.studentList = (ListView)   findViewById(R.id.student_list);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://verb-ventures-api-dev.us-east-1.elasticbeanstalk.com/api/get-admin-students/" + admin.getAccountKitId())
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
                final Student[] obtainedStudents = gson.fromJson(response.body().string(), Student[].class);

                for(int i=0; i < obtainedStudents.length; i++) {
                    obtainedStudents[i].setAdminObj(admin);
                }

                //in order to populate the list, we need to call the main UI thread again
                ManageStudentsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new StudentArrayAdapter(mcontext, obtainedStudents);
                        // Attach the adapter to a ListView
                        studentList.setAdapter(adapter);
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
    public void onStartSessionClick(View v){
         ArrayList<Student> studentsInSession = new ArrayList<Student>();

        for(int i=0; i < adapter.checked.length; i++){
            if(adapter.checked[i]){
                studentsInSession.add(adapter.getItem(i));
            }
        }
        Intent intent = new Intent(ManageStudentsActivity.this, LearnSession.class);
        intent.putExtra("admin", admin);
        intent.putExtra("studentsInSession", studentsInSession);
        intent.putExtra("totalRuns", 0);
        startActivity(intent);
    }

    public void onCreateStudentClick(View v){
        Intent intent = new Intent(ManageStudentsActivity.this, CreateStudent.class);
        intent.putExtra("admin", admin);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
