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

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
            verbPack = new VerbPack();
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
