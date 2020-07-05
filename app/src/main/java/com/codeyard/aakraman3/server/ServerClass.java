package com.codeyard.aakraman3.server;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codeyard.aakraman3.LoginActivity;
import com.codeyard.aakraman3.SignUpActivity;
import com.codeyard.aakraman3.constants.Constants;
import com.codeyard.aakraman3.constants.ServerResponseConstants;
import com.codeyard.aakraman3.models.AuthenticationModel;
import com.codeyard.aakraman3.models.UserIDModel;
import com.codeyard.aakraman3.utils.JSONUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.codeyard.aakraman3.LoginActivity.switchToMainActivity;

public class ServerClass {
    private static final String AAKRAMAN_URL = "http://35.154.171.185:5000";
    private final static String TAG = ServerClass.class.getName();
    private static String LOGIN_URL = "/api/login";
    private static String SIGNUP_URL = "/api/signup";
    private String errorMessage = "";

    public ServerClass() {
    }

    public static void sendSignUpData(Context context, String username, String password, String fullname, String phone, String email) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("fullname", fullname);
            jsonObject.put("phone", phone);
            jsonObject.put("username", username);
            jsonObject.put("password", password);

            ServerClass.sendJSONToServer(context, AAKRAMAN_URL + Constants.SIGNUP_URL, jsonObject, false);
        } catch (JSONException e) {
            Log.e("TAG", "signUp: jse", e);
        }
    }

    public static void sendLoginData(Context context, String email, String password) {

        JSONObject jsonObject = null;
        try {

            jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("password", password);

            sendJSONToServer(context, AAKRAMAN_URL + LOGIN_URL, jsonObject, false);
        } catch (JSONException e) {
            Log.e("TAG", "getLoginJSON: ", e);
        }

    }

    public static void sendContactTraceData(Context context, String myId, String otherId, String timestamp) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("myId", myId);
            jsonObject.put("otherId", otherId);
            jsonObject.put("timestamp", timestamp);
            sendJSONToServer(context, AAKRAMAN_URL + Constants.CONTACT_URL, jsonObject, true);
        } catch (JSONException e) {
            Log.e("TAG", "getContactTraceJSON: ", e);
        }
    }

    private static void sendJSONToServer(final Context context, final String URL, JSONObject jsonObject, final boolean isAuthenticationRequired) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjRequest = new JsonObjectRequest
                (Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("TAG", "onResponse: " + response.toString());
                        checkResponse(URL, response, context);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("TAG", "onErrorResponse: wtf", error);
                            }
                        }) {

            @Override
            public Map<String, String> getHeaders() {
                if (isAuthenticationRequired) {
                    Map<String, String> params = new HashMap<>();
                    AuthenticationModel authenticationModel = new AuthenticationModel(context);
                    if (authenticationModel.getAuth().equals("")) {
                        return null;
                    }

                    params.put("Authorization", "Bearer " + authenticationModel.getAuth());
                    return params;
                }
                return null;
            }
        };

        requestQueue.add(jsonObjRequest);
    }

    private static void checkResponse(String URL, JSONObject jsonObject, Context context) {
        JSONUtils jsonUtils = new JSONUtils(jsonObject);

        if (URL.contains(Constants.SIGNUP_URL)) {
            handleSignUp(jsonUtils, context);
        } else if (URL.contains(Constants.LOGIN_URL)) {
            handleLogin(jsonUtils, context);
        } else if (URL.contains(Constants.CONTACT_URL)) {
            handleContactResponse(jsonUtils, context);
        }
    }

    private static void handleContactResponse(JSONUtils jsonUtils, Context context) {
        try {
            String status = jsonUtils.getKeyValuePair(ServerResponseConstants.STATUS);
            if (status.equals(ServerResponseConstants.OK)) {
                return;

            }
        } catch (JSONException jse) {
            Log.e(TAG, "handleContactResponse: ", jse);
        }
    }

    public static void handleLogin(JSONUtils jsonUtils, Context context) {
        try {
            String status = jsonUtils.getKeyValuePair(ServerResponseConstants.STATUS);
            if (status.equals(ServerResponseConstants.LOGIN_FAIL)) {
                LoginActivity.showLoginAlert(
                        "Please try again!");
                return;
            }
            if (status.equals(ServerResponseConstants.LOGIN_FAIL_INVALID_CRED)) {
                LoginActivity.showLoginAlert(
                        "Email and password don't match .. Please try again");

                return;
            }
            String token = jsonUtils.getKeyValuePair(ServerResponseConstants.TOKEN);
            AuthenticationModel authenticationModel = new AuthenticationModel(context);
            authenticationModel.setAuth(token);
            switchToMainActivity();
        } catch (JSONException j) {
            LoginActivity.showLoginAlert("JSON EXCEPTION \n" + j.toString());
        }
    }

    private static void handleSignUp(JSONUtils jsonUtils, Context context) {
        try {

            String status = jsonUtils.getKeyValuePair(ServerResponseConstants.STATUS);
            if (status.equals(ServerResponseConstants.SIGNUP_OK)) {
//                    get BLE id and store

                String bleId = jsonUtils.getKeyValuePair(ServerResponseConstants.BLE_ID);
                UserIDModel userIDModel = new UserIDModel(context);
                userIDModel.setUserID(bleId);

                SignUpActivity.switchToLogin();
            }
            if (status.equals(ServerResponseConstants.SIGNUP_INVALID_EMAIL)) {
                SignUpActivity.showSignUpAlert(
                        "This email is already associated with some acocunt");
                return;
            }

            if (status.equals(ServerResponseConstants.SIGNUP_USER_EXISTS)) {
                SignUpActivity.showSignUpAlert(
                        "This username is already associated with some account");
                return;
            }
            if (status.equals(ServerResponseConstants.SIGNUP_INVALID_PHONE)) {
                SignUpActivity.showSignUpAlert(
                        "This phone no. seems to be invalid..");

                return;
            }
            if (status.equals(ServerResponseConstants.SIGNUP_FAIL)) {
                SignUpActivity.showSignUpAlert(
                        "Please try again!!");
            }
        } catch (JSONException je) {
            SignUpActivity.showSignUpAlert("JSON EXception please try contacting Code Yard or Aditya DEBUG");
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }
//    private static class SendServerTask extends AsyncTask<String, Void, String> {
//        @SuppressLint("StaticFieldLeak")
//        final Context mCont;
//
//        SendServerTask(Context context) {
//            this.mCont = context;
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            StringBuilder data = new StringBuilder();
//
//            HttpURLConnection httpURLConnection = null;
//            try {
//                Log.d("TAG", "doInBackground: " + params[1]);
//                FileUtil.appendToFile(params[1], mCont);
//                URL url = new URL(params[0]);
//
//                httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestMethod("POST");
//                httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
//                httpURLConnection.setRequestProperty("Accept", "application/json");
//                httpURLConnection.setDoOutput(true);
//
//                //Get auth token and if it doesn't abort
//                if (params[2].equals("TRUE")) {
//                    AuthenticationModel authenticationModel = new AuthenticationModel(mCont);
//                    if (authenticationModel.getAuth().equals("")) {
//                        Log.d("AUTH", "Authentication keynot dound");
//                        return "";
//                    }
//                    httpURLConnection.addRequestProperty("Authorization", "Bearer " + authenticationModel.getAuth());
//                }
//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes(params[1]);
//                wr.flush();
//                wr.close();
//
//                InputStream in = httpURLConnection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(in);
//
//                int inputStreamData = inputStreamReader.read();
//                while (inputStreamData != -1) {
//                    char current = (char) inputStreamData;
//                    inputStreamData = inputStreamReader.read();
//                    data.append(current);
//                }
//            } catch (NullPointerException nPE) {
//                Log.e("AAKREAMN", "doInBackground: ", nPE);
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (httpURLConnection != null) {
//                    httpURLConnection.disconnect();
//                }
//            }
//
//            return data.toString();
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            Log.e("ASDFGHJKL", "onPostExecute: starting");
//            Log.e("ASDFGHJKL", "onPostExecute: result forom server " + result);
//            Log.d("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
////            OK message place
//            FileUtil.appendToFile(result, mCont);
//            Intent intent = new Intent(Constants.NETWORK_RESPONSE);
//            intent.putExtra(Constants.HTTP_RESPONSE, result);
//
//            // broadcast the completion
//            mCont.sendBroadcast(intent);
//
//        }
//
//    }
}
