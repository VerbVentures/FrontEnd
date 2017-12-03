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
import android.view.WindowManager;
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
    private Student student;
    public static final String TAG = "CreateStudent";
    private boolean edit = false;
    private boolean delete = false;
    private boolean more = false;
    private JSONObject adminJSON;
    private JSONObject userJSON;
    private EditText etFirstName;
    private EditText etLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
        student = (Student) getIntent().getSerializableExtra("student");

        if(student != null) edit = true;


        final Button createButton = findViewById(R.id.create_student_button);
        etFirstName = findViewById(R.id.first_name_edit_text);
        etLastName = findViewById(R.id.last_name_edit_text);
        final Button deleteButton = findViewById(R.id.deleteButton);

        if(edit){
            createButton.setText("Save Student");
            etFirstName.setText(student.getUser().getFirstName());
            etLastName.setText(student.getUser().getLastName());
            deleteButton.setVisibility(View.VISIBLE);

            deleteButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    final Button yesDelete = findViewById(R.id.yesDeleteButton);
                    final Button noDelete = findViewById(R.id.noDeleteButton);
                    final TextView confirmText = findViewById(R.id.deleteConfirmText);

                    confirmText.setText("Are you sure you want to delete " + etFirstName.getText() + " " + etLastName.getText() + "?");
                    confirmText.setVisibility(View.VISIBLE);
                    yesDelete.setVisibility(View.VISIBLE);
                    noDelete.setVisibility(View.VISIBLE);

                    noDelete.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            yesDelete.setVisibility(View.GONE);
                            noDelete.setVisibility(View.GONE);
                            confirmText.setVisibility(View.GONE);
                        }
                    });

                    yesDelete.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            delete = true;
                            String url = "http://verb-ventures-api-dev.us-east-1.elasticbeanstalk.com/api/students/" + student.getStudentId() + "/";
                            makeAPIChange(url);
                            finish();
                        }
                    });
                }
            });
        }

        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url;
                if(edit){
                    url = "http://verb-ventures-api-dev.us-east-1.elasticbeanstalk.com/api/students/" + student.getStudentId() + "/";
                    makeAPIChange(url);
                    finish();
                }
                else {
                    url = "http://verb-ventures-api-dev.us-east-1.elasticbeanstalk.com/api/students/";
                    makeAPIChange(url);
                    final Button btnDone = findViewById(R.id.btnDone);
                    btnDone.setVisibility(View.VISIBLE);

                    btnDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(CreateStudent.this, ManageStudentsActivity.class);
                            intent.putExtra("admin", admin);
                            startActivity(intent);
                        }
                    });
                }

            }
        });

    }

    private void makeAPIChange(String url){
        Request request;
        OkHttpClient client;
        if(delete){
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            client = new OkHttpClient();
            request = new Request.Builder()
                    .url(url)
                    .delete()
                    .addHeader("content-type", "application/json; charset=utf-8")
                    .build();
        }
        else {

            String firstName = etFirstName.getText().toString();

            String lastName = etLastName.getText().toString();

            String accountKitId = admin.getAccountKitId();

            Log.d(TAG, "Admin = " + accountKitId);
            Log.d(TAG, "First Name = " + firstName);
            Log.d(TAG, "Last Name = " + lastName);

            // create json to post
            adminJSON = new JSONObject();
            userJSON = new JSONObject();
            try {
                userJSON.put("firstName", firstName);
                userJSON.put("lastName", lastName);
                adminJSON.put("user", userJSON);
                adminJSON.put("admin", accountKitId);
                // adminJSON.put("accountKitId", "12345"); // for testing
            } catch (JSONException e) {
                e.printStackTrace();
            }

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, adminJSON.toString());
            if(edit){
                request = new Request.Builder()
                        .url(url)
                        .put(body)
                        .addHeader("content-type", "application/json; charset=utf-8")
                        .build();
            }
            else {
                request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("content-type", "application/json; charset=utf-8")
                        .build();
            }
        }
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
                    if(!edit && !delete) {
                        Gson gson = new Gson();
                        final Student student;
                        student = gson.fromJson(responseString, Student.class);
                        student.setAdminObj(admin); // add this line
                        Log.d("Student", student.toString());

                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), student.toString() + " Created Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                        etFirstName.setText("");
                        etLastName.setText("");
                        more = true;

                    }

                    else{
                        Intent intent = new Intent(CreateStudent.this, ManageStudentsActivity.class);
                        intent.putExtra("admin", admin);
                        startActivity(intent);
                    }


                }

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
}
