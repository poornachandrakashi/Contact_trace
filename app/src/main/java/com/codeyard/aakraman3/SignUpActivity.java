package com.codeyard.aakraman3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codeyard.aakraman3.server.ServerClass;
import com.codeyard.aakraman3.utils.Util;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    static Context context;
    EditText username, name, email, phone, password;
    Button signUp;

    public static void showSignUpAlert(String message) {
        Util.showAlert(Util.createAlert(message, context));
    }

    public static void switchToLogin() {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = SignUpActivity.this;
        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        email = findViewById(R.id.uemail);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.upassword);

        findViewById(R.id.signupact_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        signUp = findViewById(R.id.SignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Util.isEmailValid(email.getText().toString())) {
                    Util.showAlert(Util.createAlert("Email is not valid.", context));
                    return;
                }
                if (username.getText().toString().length() <= 8) {
                    Util.showAlert(Util.createAlert("Username should be at least 8 characters", context));
                    return;
                }
//                if (!Util.isPasswordStrong(password.getText().toString())) {
//                    Util.showAlert(Util.createAlert("Please choose a stronger password", context));
//                    return;
//                }

                ServerClass.sendSignUpData(context,
                        username.getText().toString(),
                        password.getText().toString(),
                        name.getText().toString(),
                        phone.getText().toString(),
                        email.getText().toString());
            }
        });

    }
}
