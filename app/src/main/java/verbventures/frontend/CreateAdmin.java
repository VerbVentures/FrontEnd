package verbventures.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class CreateAdmin extends AppCompatActivity {

    public static final String TAG = "CreateAdmin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final String accountKitId = getIntent().getStringExtra("accountKitId");
        final String email = getIntent().getStringExtra("email");
        final Button button = findViewById(R.id.create_admin_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText etFirstName = findViewById(R.id.first_name_edit_text);
                String firstName = etFirstName.getText().toString();

                EditText etLastName = findViewById(R.id.last_name_edit_text);
                String lastName = etLastName.getText().toString();

                Log.d(TAG, "Account Kit Id = " + accountKitId);
                Log.d(TAG, "Last Name = " + email);
                Log.d(TAG, "First Name = " + firstName);
                Log.d(TAG, "Last Name = " + lastName);

                // create json to post
                JSONObject adminJSON = new JSONObject();
                JSONObject userJSON = new JSONObject();
                try {
                    userJSON.put("firstName", firstName);
                    userJSON.put("lastName", lastName);
                    adminJSON.put("user", userJSON);
                    adminJSON.put("accountKitId", accountKitId);
                    // adminJSON.put("accountKitId", "12345"); // for testing
                    adminJSON.put("email", email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // post data to web-server
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                String url = "http://verb-ventures-api-dev.us-east-1.elasticbeanstalk.com/api/admins/";

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
                        Intent intent = new Intent(CreateAdmin.this, LoginError.class);
                        intent.putExtra("accountKitId", accountKitId);
                        startActivity(intent);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            String responseString = response.body().string();
                            Log.d(TAG, responseString);
                            Log.e(TAG, "API Error");
                            Intent intent = new Intent(CreateAdmin.this, LoginError.class);
                            intent.putExtra("accountKitId", accountKitId);
                            startActivity(intent);
                        }
                        else {
                            String responseString = response.body().string();
                            Log.d(TAG, responseString);

                            Gson gson = new Gson();
                            Admin admin;
                            admin = gson.fromJson(responseString, Admin.class);
                            Log.d("Admin",admin.toString());
                            Intent intent = new Intent(CreateAdmin.this, ManageStudentsActivity.class);
                            intent.putExtra("admin", admin);
                            startActivity(intent);
                        }


                    }

                });
            }
        });
    }

}
