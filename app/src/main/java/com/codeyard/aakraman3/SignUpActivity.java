package com.codeyard.aakraman3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codeyard.aakraman3.constants.Constants;
import com.codeyard.aakraman3.constants.ServerResponseConstants;
import com.codeyard.aakraman3.models.UserIDModel;
import com.codeyard.aakraman3.server.ServerClass;
import com.codeyard.aakraman3.utils.JSONUtils;
import com.codeyard.aakraman3.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    EditText username, name, email, phone, password;
    Button signUp;
    Context context;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String serverResponse = intent.getStringExtra(Constants.HTTP_RESPONSE);
            Log.d("TAG", "onReceive: NETWORK " + serverResponse);
            try {
                JSONUtils jsonUtils = new JSONUtils(serverResponse);
                String status = jsonUtils.getKeyValuePair(ServerResponseConstants.STATUS);
                if (status.equals(ServerResponseConstants.SIGNUP_OK)) {
//                    get BLE id and store

                    String bleId = jsonUtils.getKeyValuePair(ServerResponseConstants.BLE_ID);
                    UserIDModel userIDModel = new UserIDModel(context);
                    userIDModel.setUserID(bleId);

                    switchToLogin();
                }
                if (status.equals(ServerResponseConstants.SIGNUP_INVALID_EMAIL)) {
                    Util.showAlert(
                            Util.createAlert(
                                    "This email is already associated with some acocunt",
                                    context));
                    return;
                }

                if (status.equals(ServerResponseConstants.SIGNUP_USER_EXISTS)) {
                    Util.showAlert(
                            Util.createAlert(
                                    "This username is already associated with some account",
                                    context));
                    return;
                }
                if (status.equals(ServerResponseConstants.SIGNUP_INVALID_PHONE)) {
                    Util.showAlert(
                            Util.createAlert(
                                    "This phone no. seems to be invalid..",
                                    context));
                    return;
                }
                if (status.equals(ServerResponseConstants.SIGNUP_FAIL)) {
                    Util.showAlert(
                            Util.createAlert(
                                    "Please try again!!",
                                    context));
                }
            } catch (JSONException je) {
                Log.e("TAG", "onReceive: se", je);
            }
        }
    };


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

//                Send data to server
                RequestQueue requestQueue = Volley.newRequestQueue(context);

//                Make the json
                JSONObject signUpJSON = ServerClass.getSignUpJSON(username.getText().toString(), password.getText().toString(), name.getText().toString(), phone.getText().toString(), email.getText().toString());
                if (signUpJSON == null) {
                    return;
                }
                final String requestBody = signUpJSON.toString();

                final String URL = Constants.AAKRAMAN_URL + Constants.SIGNUP_URL;

                // Request a json response from the provided URL
                JsonObjectRequest jsonObjRequest = new JsonObjectRequest
                        (Request.Method.POST, URL, signUpJSON, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("TAG", "onResponse: " + response.toString());
                            }
                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("TAG", "onErrorResponse: wtf", error);
                                    }
                                });

// Add the request to the RequestQueue.
                requestQueue.add(jsonObjRequest);


            }
        });

    }

    public void switchToLogin() {
        //On Successful signup...store data in server and Intent to login page
        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
//        i.putExtra(Constants.FULLNAME, name.getText().toString());
//        i.putExtra(Constants.USERNAME, username.getText().toString());
//        i.putExtra(Constants.PHONE, phone.getText().toString());
//        i.putExtra(Constants.EMAIL, email.getText().toString());
//        i.putExtra(Constants.PASSWORD, password.getText().toString());
        startActivity(i);
    }
}
