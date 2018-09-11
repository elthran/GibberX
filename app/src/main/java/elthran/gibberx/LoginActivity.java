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
                String data = String.format("{'username': '%s', 'password': '%s'}", username.getText(), password.getText());
                Request r = new Request("https://gibberx.pythonanywhere.com/login", data);
                Log.i("status code", String.valueOf(r.status_code));
                Log.i("json response", String.valueOf(r.json()));
                valid_login = r.json().optBoolean("login");
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
