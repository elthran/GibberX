package elthran.gibberx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);

        String book_id = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            book_id = (extras.getString("book_id"));
            Log.e("buttonIDonNewPage", String.valueOf(book_id));
            //The key argument here must match that used in the other activity
        }

        // Create the menu read button
        Button button_main_menu = (Button) findViewById(R.id.button_main_menu);
        // Load the actual book
        TextView book_text = (TextView) findViewById(R.id.book_text);
        String fileName = "00" + book_id;

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(fileName + ".txt")));
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll(" I ", "<b> Ich </b>");
                line = line.replaceAll("I ", "<b>Ich </b>");
                text.append(line);
                text.append('\n');
            }
            book_text.setText(Html.fromHtml(String.valueOf(text)));
        } catch (IOException e) {
            Log.e("BookError", "Eror loading book");
        }

        // Create Button on click listener for reading abook
        button_main_menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ReadBookActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
