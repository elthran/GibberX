package elthran.gibberx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;

import java.util.ArrayList;
import java.util.List;

public class BookSelectActivity extends AppCompatActivity {
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_select);

        // Get the current user pool
        CognitoUserPool userPool = new CognitoUserPool(this, IdentityManager.getDefaultIdentityManager().getConfiguration());
        // Find the user from the user pool
        CognitoUser cognitoUser = userPool.getCurrentUser();

        cognitoUser.getDetailsInBackground(getDetailsHandler);
    }

    GetDetailsHandler getDetailsHandler = new GetDetailsHandler() {
        @Override
        public void onSuccess(CognitoUserDetails cognitoUserDetails) {
            int numOfBooks = Integer.parseInt(cognitoUserDetails.getAttributes().getAttributes().get("custom:books_unlocked"));
            Log.e("Book amount:", "success start");
            createButtons(numOfBooks);
            Log.e("Book amount:", "success finish");
        }

        @Override
        public void onFailure(Exception exception) {
            Log.e("Book amount:", "failure");
        }
    };


    private void createButtons(int numberOfBooks) {
        TextView book_count = findViewById(R.id.book_count);
        LinearLayout ll = (LinearLayout)findViewById(R.id.linear_layout);
        ArrayList<String> book_list = new ArrayList<String>();
        book_list.add("The Little Prince");
        book_list.add("Alice in Wonderland");
        book_count.setText(String.valueOf(numberOfBooks));

        for (int i = 0; (i < numberOfBooks && i < book_list.size()); i++) {
            Button myButton = new Button(this);
            myButton.setText(book_list.get(i));
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
