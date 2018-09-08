package elthran.gibberx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ReadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
    }

    public void returnHome(View view) {
        Intent intent = new Intent(ReadingActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
