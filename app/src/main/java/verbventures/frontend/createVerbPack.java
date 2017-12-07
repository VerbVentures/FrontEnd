package verbventures.frontend;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import verbventures.frontend.ModelClasses.VerbPack;

public class createVerbPack extends AppCompatActivity {

    private Admin admin;
    private VerbPack verbPack;
    public static String TAG;
    public static Boolean editFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_verb_pack);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Create Verb Pack");
        TAG = this.getClass().getSimpleName();

        //get the views we need
        final EditText etVerbPack = findViewById(R.id.etVerbPackTitle);
        final Button btnCreateVerbPack = findViewById(R.id.btnCreateVerbPack);

        //get the passed in information
        admin = (Admin) getIntent().getSerializableExtra("admin");
        verbPack = (VerbPack) getIntent().getSerializableExtra("verbPack");
        if (verbPack != null) {
            // if we recieved a verb pack, we are editing
            etVerbPack.setText(verbPack.getTitle());
            editFlag = true;
            btnCreateVerbPack.setText("Save Verb Pack");
        }
        else {
            Log.d(TAG, "no verb pack detected");
            editFlag = false;
            verbPack = new VerbPack();
        }



        if (editFlag) {
            Log.d(TAG,"In the editFlag if statement");
            Button deleteButton = findViewById(R.id.deleteButton);
            deleteButton.setVisibility(View.VISIBLE);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Button yesDelete = findViewById(R.id.yesDeleteButton);
                    final Button noDelete = findViewById(R.id.noDeleteButton);
                    final TextView confirmText = findViewById(R.id.deleteConfirmText);

                    confirmText.setText("Are you sure you want to delete " + verbPack.getTitle());
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
                            String url = "http://verb-ventures-api-dev.us-east-1.elasticbeanstalk.com/api/verbpacks/" + verbPack.getVerbPackId() + "/";

                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(url)
                                    .delete()
                                    .addHeader("content-type", "application/json; charset=utf-8")
                                    .build();

                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.e("response", call.request().body().toString());
                                    Log.e(TAG, "API Error");
                                    Intent intent = new Intent(createVerbPack.this, LoginError.class);
                                    intent.putExtra("admin", admin);
                                    startActivity(intent);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (!response.isSuccessful()) {
                                        String responseString = response.body().string();
                                        Log.d(TAG, responseString);
                                        Log.e(TAG, "API Error");
                                        Intent intent = new Intent(createVerbPack.this, LoginError.class);
                                        intent.putExtra("admin", admin);
                                        startActivity(intent);
                                    }
                                    else {
                                        String responseString = response.body().string();
                                        Log.d(TAG, responseString);
                                        Intent intent = new Intent(createVerbPack.this, ManageVerbPacksActivity.class);
                                        intent.putExtra("admin", admin);
                                        startActivity(intent);
                                    }

                                }
                            });
                            finish();
                        }
                    });


                }
            });
        }

        btnCreateVerbPack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String accountKitId = admin.getAccountKitId();
                String verbPackName = etVerbPack.getText().toString();
                //set the object attributes
                verbPack.setTitle(verbPackName);
                verbPack.setAdmin(accountKitId);

                Log.d(TAG, "verb pack name: " + verbPackName );
                Log.d(TAG, "admin ID: " + accountKitId);



                Intent intent = new Intent(createVerbPack.this, selectVerbsActivity.class);
                intent.putExtra("admin", admin);
                intent.putExtra("verbPack",verbPack);
                intent.putExtra("editFlag", editFlag);
                startActivity(intent);
                finish();

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

            default:
                // if we get here, the user's action wasn't recognized
                // invoke superclass to handle it
                return super.onOptionsItemSelected(item);
        }
    }

}
