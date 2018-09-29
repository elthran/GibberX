package elthran.gibberx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheckWordListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_word_list);
        // Create the logout button
        Button button_main_menu = (Button) findViewById(R.id.return_main_menu);
        // Create word list
        TextView word_list = (TextView) findViewById(R.id.word_list);
        // Set word list
        word_list.setText("I / Ich");

        // Create Button on click listener for reading abook
        button_main_menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CheckWordListActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
