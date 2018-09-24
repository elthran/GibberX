package elthran.gibberx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class BookSelectActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_select);

        int number_of_books = 2; // Should be the level of the user

        LinearLayout ll = (LinearLayout)findViewById(R.id.linear_layout);

        for (int i = 1; i <= number_of_books; i++) {
            Button myButton = new Button(this);
            myButton.setText("Button :"+i);
            myButton.setId(i);
            myButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ll.addView(myButton);
            myButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String i = String.valueOf(myButton.getId());
                    Log.e("buttonID", String.valueOf(i));
                    Intent intent = new Intent(BookSelectActivity.this, ReadBookActivity.class);
                    intent.putExtra("book_id",i);
                    startActivity(intent);
                }
            });
        }

    }
}
