package com.codeyard.aakraman3.server;

import android.os.AsyncTask;
import android.util.Log;

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
    private static String CONTACT_URL = "/api/contact";

    private String errorMessage = "";

    public ServerClass() {
    }



    public String getErrorMessage() {
        return errorMessage;
    }

    public void sendContactTraceData(String myId, String otherId, String timestamp) {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("myId", myId);
            jsonObject.put("otherId", otherId);
            jsonObject.put("timestamp", timestamp);
            Log.d("TAG", "sendContactTraceData: " + AAKRAMAN_URL + CONTACT_URL);
            new SendServerTask().execute(AAKRAMAN_URL + CONTACT_URL, jsonObject.toString());


        } catch (JSONException e) {
            errorMessage = "";
        }
    }

    private static class SendServerTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            StringBuilder data = new StringBuilder();

            HttpURLConnection httpURLConnection = null;
            try {
                Log.d("TAG", "doInBackground: " + params[1]);

                URL url = new URL(params[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setDoOutput(true);


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
            Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }
}
