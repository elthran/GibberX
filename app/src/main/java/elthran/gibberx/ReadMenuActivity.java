package elthran.gibberx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.amazonaws.mobile.auth.core.IdentityManager;

public class ReadMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_menu);
        // Create the menu read button
        Button button_read = (Button) findViewById(R.id.button_read);
        // Create the menu read button
        Button button_main_menu = (Button) findViewById(R.id.button_main_menu);

        // Create Button on click listener for reading abook
        button_read.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ReadMenuActivity.this, BookSelectActivity.class);
                startActivity(intent);
            }
        });

        // Create Button on click listener for reading abook
        button_main_menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ReadMenuActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
