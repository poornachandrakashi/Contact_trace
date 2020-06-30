package com.codeyard.aakraman3.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {
    private JSONObject jsonObject;

    public JSONUtils(String json) throws JSONException {
        jsonObject = new JSONObject((json));
    }

    public String getKeyValuePair(String key) throws JSONException {
        return jsonObject.getString(key);
    }
}
