package elthran.gibberx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class OpenBookActivity extends AppCompatActivity {

    String bookName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_book);
        Bundle b = getIntent().getExtras();
        bookName = b.getString("bookName");
        Log.v("SecretMessage", bookName);
    }
}
