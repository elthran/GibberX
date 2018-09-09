package elthran.gibberx;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;


public class LoginActivity extends AppCompatActivity {

    private static EditText username;
    private static EditText password;
    private static TextView attempt;
    private static Button login_button;
    private Boolean valid_login;
    int attempt_counter = 5;

    public class User {

        private int id;
        private String name;
        private String password;

        public User(int id, String name, String password){
            this.id = id;
            this.name = name;
            this.password = password;
        }
    }

    public void sendPost(String name, String password) {
        Thread thread = new Thread(new Runnable() {
            @TargetApi(Build.VERSION_CODES.N)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                try {
                    URL url = new URL("http://gibberx.pythonanywhere.com/login");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);

                    Map<String,String> arguments = new HashMap<>();
                    arguments.put("username", name);
                    arguments.put("password", password); // This is a fake password obviously
                    StringJoiner sj = new StringJoiner("&");
                    for(Map.Entry<String,String> entry : arguments.entrySet())
                        sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                                + URLEncoder.encode(entry.getValue(), "UTF-8"));
                    byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
                    int length = out.length;

                    urlConnection.setFixedLengthStreamingMode(length);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                    urlConnection.connect();
                    try(OutputStream os = urlConnection.getOutputStream()) {
                        os.write(out);
                        os.flush();
                        os.close();
                    }

                    Log.i("STATUS", String.valueOf(urlConnection.getResponseCode()));
                    try {
                        BufferedReader streamReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                        StringBuilder responseStrBuilder = new StringBuilder();

                        String inputStr;
                        while ((inputStr = streamReader.readLine()) != null)
                            responseStrBuilder.append(inputStr);
                        try {
                            JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
                            Log.i("url response", jsonObject.toString());
                            valid_login = jsonObject.getBoolean("login");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginButton();
    }

    public void LoginButton() {
        username = (EditText) findViewById(R.id.editText_username);
        password = (EditText) findViewById(R.id.editText_password);
        attempt = (TextView) findViewById(R.id.textView_attempt_count);
        login_button = (Button) findViewById(R.id.button_login);

        attempt.setText(Integer.toString(attempt_counter));

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean valid_login = true;
                User user = new User(1, username.getText().toString(), password.getText().toString());
                sendPost(user.name, user.password);

                if (valid_login) {
                    Toast.makeText(LoginActivity.this, "Username and password is correct",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Username and password is NOT correct",
                            Toast.LENGTH_SHORT).show();
                    attempt_counter--;
                    attempt.setText(Integer.toString(attempt_counter));
                    if (attempt_counter == 0)
                        login_button.setEnabled(false);
                }
            }
        }
        );
    }
}
