package elthran.gibberx;

import android.content.Intent;
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
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private TextView attempt;
    private Button login_button;
    private Boolean valid_login = false;
    int attempt_counter = 5;

    public void returnToCreateOrLogin(View view) {
        Intent intent = new Intent(this, CreateOrLoginActivity.class);
        startActivity(intent);
    }

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
            @Override
            public void run() {
                try {
                    URL url = new URL("https://gibberx.pythonanywhere.com/login");
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("POST");
                    http.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    http.setRequestProperty("Accept","application/json");
                    http.setDoOutput(true);
                    http.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    try {
                        jsonParam.put("username", name);
                        jsonParam.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.i("JSON", jsonParam.toString());
                    try(DataOutputStream os = new DataOutputStream(http.getOutputStream())) {
                        os.writeBytes(jsonParam.toString());
                        os.flush();
                    }

                    Log.i("STATUS", String.valueOf(http.getResponseCode()));
                    try {
                        BufferedReader streamReader = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
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
                        http.disconnect();
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
