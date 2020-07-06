package com.codeyard.aakraman3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codeyard.aakraman3.constants.Constants;
import com.codeyard.aakraman3.constants.ServerResponseConstants;
import com.codeyard.aakraman3.models.AuthenticationModel;
import com.codeyard.aakraman3.server.ServerClass;
import com.codeyard.aakraman3.utils.JSONUtils;
import com.codeyard.aakraman3.utils.Util;

import org.json.JSONException;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    static Context context;
    EditText email, password;
    Button loginButton;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String serverResponse = intent.getStringExtra(Constants.HTTP_RESPONSE);
            try {
                JSONUtils jsonUtils = new JSONUtils(serverResponse);
                String status = jsonUtils.getKeyValuePair(ServerResponseConstants.STATUS);
                if (status.equals(ServerResponseConstants.LOGIN_FAIL)) {
                    Util.showAlert(
                            Util.createAlert(
                                    "Please try again!", context));
                    return;
                }
                if (status.equals(ServerResponseConstants.LOGIN_FAIL_INVALID_CRED)) {
                    Util.showAlert(
                            Util.createAlert(
                                    "Email and password don't match .. Please try again",
                                    context));
                    return;
                }
                //            We have to change this.. but for now lets just return the token
                String token = jsonUtils.getKeyValuePair(ServerResponseConstants.TOKEN);
                AuthenticationModel authenticationModel = new AuthenticationModel(context);
                authenticationModel.setAuth(token);
                switchToMainActivity();

            } catch (JSONException j) {
                Log.e("TAG", "onReceive: ", j);
                Util.showAlert(
                        Util.createAlert(
                                "Please try again!", context));
            }
        }
    };

    public static void showLoginAlert(String message) {
        Util.showAlert(Util.createAlert(message, context));
    }

    public static void switchToMainActivity() {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        context.registerReceiver(broadcastReceiver, new IntentFilter(Constants.NETWORK_RESPONSE));
    }

    @Override
    public void onPause() {
        super.onPause();
        context.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        TODO check if user log in.. if yes redirect to mainactivity

        context = LoginActivity.this;
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);

        findViewById(R.id.loginact_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerClass.sendLoginData(
                        context,
                        email.getText().toString(),
                        password.getText().toString());
            }
        });

    }
}
