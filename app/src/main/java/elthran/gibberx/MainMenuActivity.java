package elthran.gibberx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.*;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        // Create the logout button
        Button button_logout = findViewById(R.id.button_logout);
        // Create the menu read button
        Button button_read = findViewById(R.id.button_read);
        // Create the menu read button
        Button button_word_list = findViewById(R.id.check_words);
        // Create the user display
        TextView userName = findViewById(R.id.userDisplay);
        // Get the current user pool
        CognitoUserPool userPool = new CognitoUserPool(this, IdentityManager.getDefaultIdentityManager().getConfiguration());
        // Find the user from the user pool
        CognitoUser cognitoUser = userPool.getCurrentUser();
        // Get the user's username
        String current_username = cognitoUser.getUserId();

        PermissionsDetailsHandler permissionsDetailsHandler = new PermissionsDetailsHandler(cognitoUser);

        // Run the level checker
        cognitoUser.getDetailsInBackground(permissionsDetailsHandler);


        AWSMobileClient.getInstance().initialize(this, awsStartupResult -> {

            //Make a network call to retrieve the identity ID
            // using IdentityManager. onIdentityId happens UPon success.
            IdentityManager.getDefaultIdentityManager().getUserID(new IdentityHandler() {

                @Override
                public void onIdentityId(String s) {

                    //The network call to fetch AWS credentials succeeded, the cached
                    // user ID is available from IdentityManager throughout your app
                    Log.d("MainMenuActivity", "Identity ID is: " + s);
                    Log.d("MainMenuActivity", "Cached Identity ID: " + IdentityManager.getDefaultIdentityManager().getCachedUserID());
                    userName.setText(current_username);
                }

                @Override
                public void handleError(Exception e) {
                    Log.e("MainMenuActivity", "Error in retrieving Identity ID: " + e.getMessage());
                }
            });
        }).execute();

        // Create Button on click listener for reading abook
        button_read.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, ReadMenuActivity.class);
            startActivity(intent);
        });

        // Create Button on click listener for reading abook
        button_word_list.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, CheckWordListActivity.class);
            startActivity(intent);
        });


        // Create log out Button on click listener
        button_logout.setOnClickListener(v -> {
            IdentityManager.getDefaultIdentityManager().signOut();
            Intent intent = new Intent(MainMenuActivity.this, AuthenticatorActivity.class);
            startActivity(intent);
        });
    }
}
