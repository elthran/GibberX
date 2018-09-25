package elthran.gibberx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CheckWordListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_word_list);
        // Create the logout button
        Button button_main_menu = (Button) findViewById(R.id.return_main_menu);

        // Create Button on click listener for reading abook
        button_main_menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CheckWordListActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
