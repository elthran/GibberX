package elthran.gibberx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private TextView attempt;
    private Button create_button;
    private Boolean valid_account = false;

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

    public void sendPostCreate(String name, String password) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://gibberx.pythonanywhere.com/create_account");
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
                            valid_account = jsonObject.getBoolean("account_created");
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
        setContentView(R.layout.activity_create_account);
        CreateAccountButton();
    }

    public void returnToCreateOrLogin(View view) {
        Intent intent = new Intent(this, CreateOrLoginActivity.class);
        startActivity(intent);
    }

    public void CreateAccountButton() {
        username = (EditText) findViewById(R.id.editText_username);
        password = (EditText) findViewById(R.id.editText_password);
        create_button = (Button) findViewById(R.id.button_create_account);

        create_button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                CreateAccountActivity.User user = new CreateAccountActivity.User(1, username.getText().toString(), password.getText().toString());
                                                sendPostCreate(user.name, user.password);

                                                if (valid_account) {
                                                    Toast.makeText(CreateAccountActivity.this, "Username and password is valid",
                                                            Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(CreateAccountActivity.this, HomeActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(CreateAccountActivity.this, "Username and password is NOT valid",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
        );
    }
}
