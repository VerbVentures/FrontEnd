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
import verbventures.frontend.ModelClasses.Verb;

/**
 * Created by Jacob on 11/14/2017
 */

public class AddVerb extends AppCompatActivity {

    private Admin admin;
    private Verb verb;
    public static final String TAG = "AddVerb";
    private boolean edit = false;
    private boolean delete = false;
    private EditText etVerb;
    private EditText etDefinition;
    private JSONObject verbJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_verb);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        admin = (Admin) getIntent().getSerializableExtra("admin");
        verb = (Verb) getIntent().getSerializableExtra("verb");

        if(verb != null) edit = true;

        final Button createButton = findViewById(R.id.createButton);
        etVerb = findViewById(R.id.etVerb);
        etDefinition = findViewById(R.id.etDefinition);
        final Button deleteButton = findViewById(R.id.deleteButton);


        if(edit){
            createButton.setText("Save Verb");
            etVerb.setText(verb.getVerb());
            etDefinition.setText(verb.getDefinition());
            deleteButton.setVisibility(View.VISIBLE);

            deleteButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    final Button yesDelete = findViewById(R.id.yesDeleteButton);
                    final Button noDelete = findViewById(R.id.noDeleteButton);
                    final TextView confirmText = findViewById(R.id.deleteConfirmText);

                    confirmText.setText("Are you sure you want to delete " + etVerb.getText() + "?");
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
                            String url = "http://verb-ventures-api-dev.us-east-1.elasticbeanstalk.com/api/verbs/" + verb.getVerbId() + "/";
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
                    url = "http://verb-ventures-api-dev.us-east-1.elasticbeanstalk.com/api/verbs/" + verb.getVerbId() + "/";
                    makeAPIChange(url);
                    finish();
                }
                else {
                    url = "http://verb-ventures-api-dev.us-east-1.elasticbeanstalk.com/api/verbs/";
                    makeAPIChange(url);
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

            String verbString = etVerb.getText().toString();

            String def = etDefinition.getText().toString();

            String accountKitId = admin.getAccountKitId();

            Log.d(TAG, "Admin = " + accountKitId);
            Log.d(TAG, "First Name = " + verbString);
            Log.d(TAG, "Last Name = " + def);

            // create json to post
            verbJSON = new JSONObject();
            try {
                verbJSON.put("verb", verb);
                verbJSON.put("definition", def);
                verbJSON.put("admin", accountKitId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, verbJSON.toString());
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
                Intent intent = new Intent(AddVerb.this, LoginError.class);
                intent.putExtra("admin", admin);
                startActivity(intent);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    String responseString = response.body().string();
                    Log.d(TAG, responseString);
                    Log.e(TAG, "API Error");
                    Intent intent = new Intent(AddVerb.this, LoginError.class);
                    intent.putExtra("admin", admin);
                    startActivity(intent);
                }
                else {
                    String responseString = response.body().string();
                    Log.d(TAG, responseString);
                    if(!edit && !delete) {
                        Gson gson = new Gson();
                        final Verb verb;
                        verb = gson.fromJson(responseString, Verb.class);
                        Log.d("Verb", verb.toString());

                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), verb.toString() + " Created Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                        etVerb.setText("");
                        etDefinition.setText("");
                    }

                    else{
                        Intent intent;
                        if(edit)
                            intent = new Intent(AddVerb.this, ManageVerbsActivity.class);
                        else
                            intent = new Intent(AddVerb.this, AddAnimation.class);

                        intent.putExtra("admin", admin);
                        intent.putExtra("verb", verb);
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
