package com.codeyard.aakraman3.server;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerClass {
    private static final String AAKRAMAN_URL = "http://35.154.171.185:5000";

    private static String LOGIN_URL = "/api/login";
    private static String SIGNUP_URL = "/api/signup";

    private String errorMessage = "";

    public ServerClass() {
    }

    public static JSONObject getSignUpJSON(String username, String password, String fullname, String phone, String email) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("fullname", fullname);
            jsonObject.put("phone", phone);
            jsonObject.put("username", username);
            jsonObject.put("password", password);

        } catch (JSONException e) {
            Log.e("TAG", "signUp: jse", e);
        }
        return jsonObject;
    }

    public static JSONObject getLoginJSON(String email, String password) {

        JSONObject jsonObject = null;
        try {

            jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            Log.e("TAG", "getLoginJSON: ", e);
        }
        return jsonObject;

    }

    public static JSONObject getContactTraceJSON(String myId, String otherId, String timestamp) {
        JSONObject jsonObject = null;

        try {

            jsonObject = new JSONObject();
            jsonObject.put("myId", myId);
            jsonObject.put("otherId", otherId);
            jsonObject.put("timestamp", timestamp);

        } catch (JSONException e) {
            Log.e("TAG", "getContactTraceJSON: ", e);
        }
        return jsonObject;
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
