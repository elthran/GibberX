package elthran.gibberx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OpenBookActivity extends AppCompatActivity {

    String bookName;
    String fileName;
    TextView currentTitle;
    TextView currentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_book);
        Bundle b = getIntent().getExtras();
        bookName = b.getString("bookName");
        fileName = bookName.replaceAll(" ", "_").toLowerCase();
        currentTitle = (TextView) findViewById(R.id.currentTitle);
        currentTitle.setText(bookName);
        currentText = (TextView) findViewById(R.id.currentText);

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(fileName + ".txt")));
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            currentText.setText(text);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error reading file!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void returnToBookSelection(View view) {
        Intent intent = new Intent(this, ChooseBookActivity.class);
        startActivity(intent);
    }
}
