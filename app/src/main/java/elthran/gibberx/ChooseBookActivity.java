package elthran.gibberx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ChooseBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_book);
    }

    public void returnToReadingMenu(View view) {
        Intent intent = new Intent(ChooseBookActivity.this, ReadingMenuActivity.class);
        startActivity(intent);
    }

    public void openBook(View view) {
        String bookName = (String) view.getTag();
        Intent intent = new Intent(ChooseBookActivity.this, OpenBookActivity.class);
        intent.putExtra("bookName", bookName);
        startActivity(intent);
    }
}
