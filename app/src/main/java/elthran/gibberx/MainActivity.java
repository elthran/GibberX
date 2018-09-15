package elthran.gibberx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.amazonaws.mobile.auth.core.IdentityManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create log out Button on click listener
        Button clickButton = (Button) findViewById(R.id.button_logout);
        clickButton.setOnClickListener( new View.OnClickListener() {

            public void onClick(View v) {
                IdentityManager.getDefaultIdentityManager().signOut();
                Intent intent = new Intent(MainActivity.this, AuthenticatorActivity.class);
                startActivity(intent);
            }
        });
    }
}
