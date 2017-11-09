package verbventures.frontend;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.AccountPreferences;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.UIManager;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import okhttp3.ResponseBody;
import verbventures.frontend.ModelClasses.Admin;

public class MainActivity extends AppCompatActivity {

    public static int APP_REQUEST_CODE = 99;
    public static final String TAG = "MainActivity";
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCurrentAccount();

    }

    private void getCurrentAccount(){
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if (accessToken != null) {
            //Handle Returning User
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    // Get Account Kit ID
                    final String accountKitId = account.getId();
                    Log.e("Account Kit Id", accountKitId);

                    if(account.getEmail()!=null)
                        Log.e("Email",account.getEmail());

                    String mUrlString = "http://verb-ventures-api-dev.us-east-1.elasticbeanstalk.com//api/admins/" + accountKitId;
                    Request request = new Request.Builder()
                        .url(mUrlString)
                        .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override public void onFailure(Call call, IOException e) {
                            Log.e(TAG, "API Error", e);
                            Intent intent = new Intent(MainActivity.this, LoginError.class);
                            intent.putExtra("accountKitId", accountKitId);
                            startActivity(intent);
                        }

                        @Override public void onResponse(Call call, Response response) throws IOException {
                            if (response.code() == 404) {
                                // Go to CreateAdmin Activity
                                Log.d(TAG, "New Admin");
                                Intent intent = new Intent(MainActivity.this, CreateAdmin.class);
                                intent.putExtra("accountKitId", accountKitId);
                                startActivity(intent);
                            }
                            if (!response.isSuccessful()) {
                                Log.e(TAG, "API Error");
                                Intent intent = new Intent(MainActivity.this, LoginError.class);
                                intent.putExtra("accountKitId", accountKitId);
                                startActivity(intent);
                            }
                            String responseString = response.body().string();
                            Log.d(TAG, responseString);

                            Gson gson = new Gson();
                            Admin admin;
                            admin = gson.fromJson(responseString, Admin.class);
                            Log.d("Admin",admin.toString());
                            Intent intent = new Intent(MainActivity.this, MainScreen.class);
                            intent.putExtra("admin", admin);
                            startActivity(intent);

                        }
                    });

                }

                @Override
                public void onError(final AccountKitError error) {
                    // Handle Error
                    Log.e(TAG,error.toString());
                    Intent intent = new Intent(MainActivity.this, LoginError.class);
                    startActivity(intent);
                }
            });
        }
        else {
            Log.e(TAG,"Logged Out User");
            emailLogin(null);
        }
    }

    public void emailLogin(@Nullable View view) {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder = new AccountKitConfiguration.AccountKitConfigurationBuilder(
                LoginType.EMAIL,AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.CODE
        UIManager uiManager = new SkinManager(
                SkinManager.Skin.CLASSIC,
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? getResources().getColor(R.color.colorPrimary,null):getResources().getColor(R.color.colorPrimary)),
                R.drawable.background,
                SkinManager.Tint.BLACK,
                0.55
        );

        configurationBuilder.setUIManager(uiManager);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(final int requestCode,final int resultCode,final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE && resultCode == RESULT_OK) {
            getCurrentAccount();
        }
    }

    public void logout(@Nullable View view){
        AccountKit.logOut();
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if(accessToken!=null)
            Log.e(TAG,"Still Logged in...");
        else
            Toast.makeText(this,"Logged Out",Toast.LENGTH_SHORT).show();
    }
}
