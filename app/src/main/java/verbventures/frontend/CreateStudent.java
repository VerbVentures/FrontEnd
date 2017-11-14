package verbventures.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import verbventures.frontend.ModelClasses.Admin;
import verbventures.frontend.ModelClasses.Student;

/**
 * Created by Nathan on 11/14/2017.
 */

public class CreateStudent extends AppCompatActivity {

    private Admin admin;
    public static final String TAG = "CreateStudent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student);
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        admin = (Admin) getIntent().getSerializableExtra("admin");

        final Button button = findViewById(R.id.create_student_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final EditText etFirstName = findViewById(R.id.first_name_edit_text);
                String firstName = etFirstName.getText().toString();

                final EditText etLastName = findViewById(R.id.last_name_edit_text);
                String lastName = etLastName.getText().toString();

                String accountKitId = admin.getAccountKitId();

                Log.d(TAG, "Admin = " + accountKitId);
                Log.d(TAG, "First Name = " + firstName);
                Log.d(TAG, "Last Name = " + lastName);

                // create json to post
                JSONObject adminJSON = new JSONObject();
                JSONObject userJSON = new JSONObject();
                try {
                    userJSON.put("firstName", firstName);
                    userJSON.put("lastName", lastName);
                    adminJSON.put("user", userJSON);
                    adminJSON.put("admin", accountKitId);
                    // adminJSON.put("accountKitId", "12345"); // for testing
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // post data to web-server
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                String url = "http://verb-ventures-api-dev.us-east-1.elasticbeanstalk.com/api/students/";

                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, adminJSON.toString());
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("content-type", "application/json; charset=utf-8")
                        .build();

                client.newCall(request).enqueue(new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("response", call.request().body().toString());
                        Log.e(TAG, "API Error");
                        Intent intent = new Intent(CreateStudent.this, LoginError.class);
                        intent.putExtra("admin", admin);
                        startActivity(intent);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            String responseString = response.body().string();
                            Log.d(TAG, responseString);
                            Log.e(TAG, "API Error");
                            Intent intent = new Intent(CreateStudent.this, LoginError.class);
                            intent.putExtra("admin", admin);
                            startActivity(intent);
                        }
                        else {
                            String responseString = response.body().string();
                            Log.d(TAG, responseString);

                            Gson gson = new Gson();
                            final Student student;
                            student = gson.fromJson(responseString, Student.class);
                            Log.d("Student", student.toString());

                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),student.toString() + " Created Successfully",Toast.LENGTH_SHORT).show();
                                }
                            });

                            etFirstName.setText("");
                            etLastName.setText("");

                        }


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

            default:
                // if we get here, the user's action wasn't recognized
                // invoke superclass to handle it
                return super.onOptionsItemSelected(item);
        }
    }
}
