package verbventures.frontend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import verbventures.frontend.ModelClasses.Admin;
import verbventures.frontend.ModelClasses.Student;
import verbventures.frontend.ModelClasses.Verb;
import verbventures.frontend.ModelClasses.VerbPack;

public class selectVerbsActivity extends AppCompatActivity {

     static Admin admin;
     static VerbPack verbPack;
     static boolean editFlag;
     private VerbArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_verbs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String TAG = "debug";

        final Context mcontext = this;
        admin = (Admin) getIntent().getSerializableExtra("admin");
        verbPack = (VerbPack) getIntent().getSerializableExtra("verbPack");
        editFlag = (boolean) getIntent().getBooleanExtra("editFlag", false);

        //get the views we need
        final ListView listView = (ListView) findViewById(R.id.lvVerbList);
        final Button btnFinish = (Button) findViewById(R.id.btnFinish);

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
                selectVerbsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new VerbArrayAdapter(mcontext, obtainedVerbs, verbPack.getVerbPackVerbs());
                        // Attach the adapter to a ListView
                        listView.setAdapter(adapter);
                    }
                });

            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String accountKitId = admin.getAccountKitId();
                List<String> verbsInPack = new ArrayList<>();

                Log.d(TAG, "Admin = " + accountKitId);
                Log.d(TAG, "verbPack name:" + verbPack.getTitle());

                for(int i=0; i < adapter.checked.length; i++){
                    if(adapter.checked[i]){
                        verbsInPack.add(adapter.getItem(i).getVerbId());
                    }
                }

                //create list with admin id in it
                List<String> userVerbPacks = new ArrayList<>();
                userVerbPacks.add(admin.getUser().getUserId());

                //create JSONArrays for the data
                JSONArray verbsInPackJSON = new JSONArray();
                JSONArray userVerbPacksJSON = new JSONArray();
                for (String verbId: verbsInPack) {
                    verbsInPackJSON.put(verbId);
                }
                for (String assignId : userVerbPacks) {
                    userVerbPacksJSON.put(assignId);
                }


                // create json to post
                JSONObject verbPackJSON = new JSONObject();
                try {
                    verbPackJSON.put("title", verbPack.getTitle());
                    verbPackJSON.put("admin", verbPack.getAdmin());
                    verbPackJSON.put("verbPackVerbs", verbsInPackJSON);
                    verbPackJSON.put("userVerbPacks", userVerbPacksJSON);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "admin JSON object: " + verbPackJSON.toString());

                OkHttpClient client = new OkHttpClient();
                Request request;
                if (editFlag) {
                    //if we are editing a verb, we need to do a PUT
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    String url = "http://verb-ventures-api-dev.us-east-1.elasticbeanstalk.com/api/verbpacks/" + verbPack.getVerbPackId();
                    RequestBody body = RequestBody.create(JSON, verbPackJSON.toString());
                    request = new Request.Builder()
                            .url(url)
                            .put(body)
                            .addHeader("content-type", "application/json; charset=utf-8")
                            .build();
                } else {
                    //if we are not editing, we only need to do a POST
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    String url = "http://verb-ventures-api-dev.us-east-1.elasticbeanstalk.com/api/verbpacks/";
                    RequestBody body = RequestBody.create(JSON, verbPackJSON.toString());
                    request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .addHeader("content-type", "application/json; charset=utf-8")
                            .build();
                }
                // post data to web-server


                client.newCall(request).enqueue(new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("response", call.request().body().toString());
                        Log.e(TAG, "API Error");
                        Intent intent = new Intent(selectVerbsActivity.this, LoginError.class);
                        intent.putExtra("admin", admin);
                        startActivity(intent);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            String responseString = response.body().string();
                            Log.d(TAG, responseString);
                            Log.e(TAG, "API Error");
                            Intent intent = new Intent(selectVerbsActivity.this, LoginError.class);
                            intent.putExtra("admin", admin);
                            startActivity(intent);
                        }
                        else {
                            String responseString = response.body().string();
                            Log.d(TAG, responseString);

                            Gson gson = new Gson();
                            final VerbPack verbPack;
                            verbPack = gson.fromJson(responseString, VerbPack.class);
                            Log.d("verbPack", verbPack.getTitle());

                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),verbPack.getTitle() + " Created Successfully",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }


                    }

                });

            }
        });


    }

}
