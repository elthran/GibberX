package elthran.gibberx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ReadingMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_menu);
    }

    public void returnHome(View view) {
        Intent intent = new Intent(ReadingMenuActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void chooseBook(View view) {
        Intent intent = new Intent(ReadingMenuActivity.this, ChooseBookActivity.class);
        startActivity(intent);
    }
}
