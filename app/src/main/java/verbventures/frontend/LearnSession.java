package verbventures.frontend;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import verbventures.frontend.ModelClasses.Admin;
import verbventures.frontend.ModelClasses.InvalidAnimations;
import verbventures.frontend.ModelClasses.Student;
import verbventures.frontend.ModelClasses.RandomVerb;
import verbventures.frontend.ModelClasses.Verb;

public class LearnSession extends AppCompatActivity {

    private ListView studentList;
    private StudentArrayAdapter adapter;
    private ImageView topLeft;
    private ImageView topRight;
    private ImageView bottomLeft;
    private ImageView bottomRight;
    public static final String TAG ="debug";
    public ImageView imageView1;
    public ImageView imageView2;
    public ImageView imageView3;
    public ImageView imageView4;
    public String[] arrayOfImages = new String[4];
    public RandomVerb testGson;
    //public InvalidAnimations testGson2;
    private Admin admin;
    private int totalRuns;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_session);
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().hide();

        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });

        final Button btn_exit = findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final TextView tv_verb = findViewById(R.id.verb_text_view);
        tv_verb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                speak(tv_verb.getText().toString());
            }
        });


        final Context mcontext = this;
        admin = (Admin) getIntent().getSerializableExtra("admin");
        totalRuns = (int) getIntent().getSerializableExtra("totalRuns");
        final ArrayList<Student> studentsInSession = (ArrayList<Student>) getIntent().getSerializableExtra("studentsInSession");


            String key = studentsInSession.get(0).getStudentId();

            // Create the client and form the request
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://verb-ventures-api-dev.us-east-1.elasticbeanstalk.com/api/get-random-verb/" + key)
                    .build();

            // Call the client enqueue with a callback function
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "request unsuccessful");
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //Log.d(TAG, response.body().string());
                    String json = response.body().string();
                    Gson gson = new Gson();
                    Log.d(TAG, "json is " + json);
                    testGson = gson.fromJson(json, RandomVerb.class);
                    Log.d(TAG, "HEEEEEEEEEEEEEEERE");
                    //testGson2 = gson.fromJson(json, InvalidAnimations.class);
                    Log.d(TAG, "HEEEEEEEEEEEEEEERE22222");
                    //Log.d(TAG, "@@@@@@@@@@@@@" + testGson2.getImageAddress());
                    //List<InvalidAnimations> test3 = Arrays.asList(testGson2);
                    //Log.d(TAG, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + testGson.getInvalidAnimations().get(1).getImageAddress());


                    LearnSession.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView = findViewById(R.id.verb_text_view);
                            TextView textViewStudent = findViewById(R.id.student_text_view);
                            textView.setText(testGson.getSelectedVerb().getVerb());
                            textViewStudent.setText("It is currently " + studentsInSession.get(0) + "'s turn!");
                            speak(testGson.getSelectedVerb().getVerb());
                            topLeft = (ImageView) findViewById(R.id.imageView1);
                            topRight = (ImageView) findViewById(R.id.imageView2);
                            bottomLeft = (ImageView) findViewById(R.id.imageView3);
                            bottomRight = (ImageView) findViewById(R.id.imageView4);
                            arrayOfImages[0] = testGson.getCorrectAnimation().getImageAddress();
                            arrayOfImages[1] = testGson.getInvalidAnimations().get(0).getImageAddress();
                            arrayOfImages[2] = testGson.getInvalidAnimations().get(1).getImageAddress();
                            arrayOfImages[3] = testGson.getInvalidAnimations().get(2).getImageAddress();
                            Collections.shuffle(Arrays.asList(arrayOfImages));

                            Glide
                                    .with(mcontext)
                                    .load(arrayOfImages[0])
                                    .asGif()
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    //.error()
                                    .into(topLeft);
                            Glide
                                    .with(mcontext)
                                    .load(arrayOfImages[1])
                                    .asGif()
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    //.error()
                                    .into(topRight);
                            Glide
                                    .with(mcontext)
                                    .load(arrayOfImages[2])
                                    .asGif()
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    //.error()
                                    .into(bottomLeft);
                            Glide
                                    .with(mcontext)
                                    .load(arrayOfImages[3])
                                    .asGif()
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    //.error()
                                    .into(bottomRight);
                        }
                    });


                }
            });

            Log.d(TAG, "total runs is !!!!!!!!!!!! " + totalRuns);
            final View imageButton1 = findViewById(R.id.imageButton1);
            imageButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String correct = testGson.getCorrectAnimation().getImageAddress();
                    if (correct.equals(arrayOfImages[0])) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "You are correct!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Collections.rotate(studentsInSession, -1);
                        Intent intent = getIntent();
                        totalRuns++;
                        intent.putExtra("totalRuns", totalRuns);
                        if(totalRuns == studentsInSession.size() * 2) {
                            Intent manageStudents = new Intent(mcontext, ManageStudentsActivity.class);
                            manageStudents.putExtra("admin", admin);
                            startActivity(manageStudents);
                            finish();
                        }
                        else {
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        imageView1.setVisibility(view.INVISIBLE);
                    }
                }
            });
            final View imageButton2 = findViewById(R.id.imageButton2);
            imageButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String correct = testGson.getCorrectAnimation().getImageAddress();
                    if (correct.equals(arrayOfImages[1])) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "You are correct!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Collections.rotate(studentsInSession, -1);
                        Intent intent = getIntent();
                        totalRuns++;
                        intent.putExtra("totalRuns", totalRuns);
                        if(totalRuns == studentsInSession.size() * 2) {
                            Intent manageStudents = new Intent(mcontext, ManageStudentsActivity.class);
                            manageStudents.putExtra("admin", admin);
                            startActivity(manageStudents);
                            finish();
                        }
                        else {
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        imageView2.setVisibility(view.INVISIBLE);
                    }
                }
            });
            final View imageButton3 = findViewById(R.id.imageButton3);
            imageButton3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String correct = testGson.getCorrectAnimation().getImageAddress();
                    if (correct.equals(arrayOfImages[2])) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "You are correct!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Collections.rotate(studentsInSession, -1);
                        Intent intent = getIntent();
                        totalRuns++;
                        intent.putExtra("totalRuns", totalRuns);
                        if(totalRuns == studentsInSession.size() * 2) {
                            Intent manageStudents = new Intent(mcontext, ManageStudentsActivity.class);
                            manageStudents.putExtra("admin", admin);
                            startActivity(manageStudents);
                            finish();
                        }
                        else {
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        imageView3.setVisibility(view.INVISIBLE);
                    }
                }
            });
            final View imageButton4 = findViewById(R.id.imageButton4);
            imageButton4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String correct = testGson.getCorrectAnimation().getImageAddress();
                    if (correct.equals(arrayOfImages[3])) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "You are correct!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Collections.rotate(studentsInSession, -1);
                        Intent intent = getIntent();
                        totalRuns++;
                        intent.putExtra("totalRuns", totalRuns);
                        if(totalRuns == studentsInSession.size() * 2) {
                            Intent manageStudents = new Intent(mcontext, ManageStudentsActivity.class);
                            manageStudents.putExtra("admin", admin);
                            startActivity(manageStudents);
                            finish();
                        }
                        else {
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        imageView4.setVisibility(view.INVISIBLE);
                    }
                }
            });
    }

    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        tts.shutdown();
    }

}
