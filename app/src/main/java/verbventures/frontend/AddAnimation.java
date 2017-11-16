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

public class AddAnimation extends AppCompatActivity {


    private Admin admin;
    private Verb verb;
    public static final String TAG = "AddAnimation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_animation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        admin = (Admin) getIntent().getSerializableExtra("admin");
        verb = (Verb) getIntent().getSerializableExtra("verb");
        final TextView verbTextView = findViewById(R.id.verb_text_view);
        verbTextView.setText(verb.getVerb());
        final Button button = findViewById(R.id.add_animation_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final EditText etAnimationURL1 = findViewById(R.id.animation_edit_text1);
                String animationURL1 = etAnimationURL1.getText().toString();

                final EditText etAnimationURL2 = findViewById(R.id.animation_edit_text2);
                String animationURL2 = etAnimationURL1.getText().toString();

                final EditText etAnimationURL3 = findViewById(R.id.animation_edit_text3);
                String animationURL3 = etAnimationURL1.getText().toString();

                final EditText etAnimationURL4 = findViewById(R.id.animation_edit_text4);
                String animationURL4 = etAnimationURL1.getText().toString();

                String accountKitId = admin.getAccountKitId();
                String verbId = verb.getVerbId();

                Log.d(TAG, "Admin = " + accountKitId);
                Log.d(TAG, "Verb = " + verb);
                Log.d(TAG, "URL 1 = " + animationURL1);
                Log.d(TAG, "URL 2 = " + animationURL2);
                Log.d(TAG, "URL 3 = " + animationURL3);
                Log.d(TAG, "URL 4 = " + animationURL4);

                String[] addresses = new String[4];
                addresses[0] = animationURL1;
                addresses[1] = animationURL2;
                addresses[2] = animationURL3;
                addresses[3] = animationURL4;

                // create json to post
                for(int i = 0; i < addresses.length; i++) {
                    JSONObject animationJSON = new JSONObject();
                    try {
                        animationJSON.put("verb", verbId);
                        animationJSON.put("imageAddress", addresses[i]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // post data to web-server
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    String url = "http://verb-ventures-api-dev.us-east-1.elasticbeanstalk.com/api/animations/";

                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = RequestBody.create(JSON, animationJSON.toString());
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
                            Intent intent = new Intent(AddAnimation.this, LoginError.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (!response.isSuccessful()) {
                                String responseString = response.body().string();
                                Log.d(TAG, responseString);
                                Log.e(TAG, "API Error");
                                Intent intent = new Intent(AddAnimation.this, LoginError.class);
                                intent.putExtra("admin", admin);
                                startActivity(intent);
                            } else {
                                String responseString = response.body().string();
                                Log.d(TAG, responseString);

                                Gson gson = new Gson();
                                final Verb verb;
                                verb = gson.fromJson(responseString, Verb.class);
                                Log.d("Verb", verb.toString());

                            }


                        }

                    });
                }
                Intent manageVerbs = new Intent(AddAnimation.this, ManageVerbsActivity.class);
                manageVerbs.putExtra("admin", admin);
                startActivity(manageVerbs);
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
