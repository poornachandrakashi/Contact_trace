package com.codeyard.aakraman3.server;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.codeyard.aakraman3.AuthenticationModel;
import com.codeyard.aakraman3.Constants;
import com.codeyard.aakraman3.FileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerClass {
    private static final String AAKRAMAN_URL = "http://35.154.171.185:5000";

    private static String LOGIN_URL = "/api/login";
    private static String SIGNUP_URL = "/api/signup";

    private String errorMessage = "";

    public ServerClass() {
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public void signUp(Context context, String username, String password, String fillname, String phone, String email) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("fullname", fillname);
            jsonObject.put("phone", phone);
            jsonObject.put("username", username);
            jsonObject.put("password", password);

            new SendServerTask(context).execute(AAKRAMAN_URL + SIGNUP_URL, jsonObject.toString(), "FALSE");
        } catch (JSONException e) {
            Log.e("TAG", "signUp: jse", e);
        }
    }

    public void sendLogin(Context context, String email, String password) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            new SendServerTask(context).execute(AAKRAMAN_URL + LOGIN_URL, jsonObject.toString(), "FALSE");
        } catch (JSONException e) {
            Log.e("TAG", "login: jse", e);
        }
    }

    public void sendContactTraceData(String myId, String otherId, String timestamp, Context conted) {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("myId", myId);
            jsonObject.put("otherId", otherId);
            jsonObject.put("timestamp", timestamp);
            String CONTACT_URL = "/api/contact/secure";
            Log.d("TAG", "sendContactTraceData: " + AAKRAMAN_URL + CONTACT_URL);
            new SendServerTask(conted).execute(AAKRAMAN_URL + CONTACT_URL, jsonObject.toString(), "TRUE");


        } catch (JSONException e) {
            errorMessage = "";
        }
    }

    private static class SendServerTask extends AsyncTask<String, Void, String> {
        @SuppressLint("StaticFieldLeak")
        final Context mCont;

        SendServerTask(Context context) {
            this.mCont = context;
        }

        @Override
        protected String doInBackground(String... params) {

            StringBuilder data = new StringBuilder();

            HttpURLConnection httpURLConnection = null;
            try {
                Log.d("TAG", "doInBackground: " + params[1]);
                FileUtil.appendToFile(params[1], mCont);
                URL url = new URL(params[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setDoOutput(true);

                //Get auth token and if it doesnt abort
                if (params[2].equals("TRUE")) {
                    AuthenticationModel authenticationModel = new AuthenticationModel(mCont);
                    if (authenticationModel.getAuth().equals("")) {
                        Toast.makeText(mCont, "Authentication keynot dound", Toast.LENGTH_SHORT).show();
                        return "";
                    }
                    httpURLConnection.addRequestProperty("Authorization", "Bearer " + authenticationModel.getAuth());
                }
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(params[1]);
                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data.append(current);
                }
            } catch (NullPointerException nPE) {
                Log.e("AAKREAMN", "doInBackground: ", nPE);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
//            OK message place
            FileUtil.appendToFile(result, mCont);
            Intent intent = new Intent(Constants.NETWORK_RESPONSE);
            intent.putExtra(Constants.HTTP_RESPONSE, result);

            // broadcast the completion
            mCont.sendBroadcast(intent);

        }

    }
}
