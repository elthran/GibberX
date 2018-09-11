package elthran.gibberx;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class POSTThread extends Thread {
    private JSONObject data;
    private JSONObject response;
    private HttpURLConnection http;
    private URL url;

    POSTThread (String urlString, JSONObject data) {
        try {
            this.url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.data = data;
    }

    public void run() {
        try {
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            http.setRequestProperty("Accept","application/json");
            http.setDoOutput(true);
            http.setDoInput(true);

            try {
                try(DataOutputStream os = new DataOutputStream(http.getOutputStream())) {
                    os.writeBytes(this.data.toString());
                    os.flush();
                    os.close();
                }
                try {
                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
                    StringBuilder responseStrBuilder = new StringBuilder();

                    String inputStr;
                    while ((inputStr = streamReader.readLine()) != null)
                        responseStrBuilder.append(inputStr);
                    this.response = new JSONObject(responseStrBuilder.toString());
                } finally {
                    http.disconnect();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getResponse() {
        return response;
    }

    public int getResponseCode() {
        try {
            return http.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}

public class Request {
    // constructor
    private JSONObject response;
    int status_code;

    Request(String url, String data) {
        try {
            JSONObject jsonData = new JSONObject(data);
//            Log.i("Json value", String.valueOf(jsonData.get("username")));
            POSTThread thread = new POSTThread(url, jsonData);
            thread.start();
            try {
                thread.join();
                this.response = thread.getResponse();
                this.status_code = thread.getResponseCode();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject json() {
        return response;
    }

    public String jsonString() {
        return response.toString();
    }
}
