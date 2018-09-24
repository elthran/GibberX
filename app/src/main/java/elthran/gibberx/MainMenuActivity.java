package elthran.gibberx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.*;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler;

import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        // Create the logout button
        Button button_logout = (Button) findViewById(R.id.button_logout);
        // Create the menu read button
        Button button_read = (Button) findViewById(R.id.button_read);
        // Create the user display
        TextView userName = (TextView) findViewById(R.id.userDisplay);
        // Get the current user pool
        CognitoUserPool userPool = new CognitoUserPool(this, IdentityManager.getDefaultIdentityManager().getConfiguration());
        // Find the user from the user pool
        CognitoUser cognitoUser = userPool.getCurrentUser();
        // Get the user's username
        String current_username = cognitoUser.getUserId();

        // Implement callback handler for adding/changing attributes
        UpdateAttributesHandler updateAttributesHandler = new UpdateAttributesHandler() {
            @Override
            public void onSuccess(List<CognitoUserCodeDeliveryDetails> attributesVerificationList) {
                Log.e("updateAttributesHandler", "success");
            }
            @Override
            public void onFailure(Exception exception) {
                Log.e("updateAttributesHandler", "failure");
            }
        };

        // Check current level. Set it to one if non-existant
        GetDetailsHandler checkAllPermissionsAreCreated = new GetDetailsHandler() {
            @Override
            public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                // Create an empty attribute class
                CognitoUserAttributes check_permissions = new CognitoUserAttributes();
                final String books_unlocked = cognitoUserDetails.getAttributes().getAttributes().get("custom:books_unlocked");
                final String words_unlocked = cognitoUserDetails.getAttributes().getAttributes().get("custom:words_unlocked");
                // Add attributes to be changed to the class
                check_permissions.addAttribute("custom:books_unlocked", "1");
                check_permissions.addAttribute("custom:words_unlocked", "1");
                if ((books_unlocked == null) || (Integer.parseInt(books_unlocked) < 1)) {
                    cognitoUser.updateAttributesInBackground(check_permissions, updateAttributesHandler);
                    Log.e("checkLevelHandler", "Attribute books_unlocked being set to 1.");
                } else {
                    Log.e("checkLevelHandler", "Attribute books_unlocked passed test");
                }
                if ((words_unlocked == null) || (Integer.parseInt(words_unlocked) < 1)) {
                    cognitoUser.updateAttributesInBackground(check_permissions, updateAttributesHandler);
                    Log.e("checkLevelHandler", "Attribute words_unlocked being set to 1.");
                } else {
                    Log.e("checkLevelHandler", "Attribute words_unlocked passed test");
                }
            }
            @Override
            public void onFailure(Exception exception) {
                // Fetch user details failed, check exception for the cause
                Log.e("checkLevelHandler", "failure");
            }
        };

        // Run the level checker
        cognitoUser.getDetailsInBackground(checkAllPermissionsAreCreated);

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {

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
            }
        }).execute();

        // Create Button on click listener for reading abook
        button_read.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ReadMenuActivity.class);
                startActivity(intent);
            }
        });

        // Create log out Button on click listener
        button_logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IdentityManager.getDefaultIdentityManager().signOut();
                Intent intent = new Intent(MainMenuActivity.this, AuthenticatorActivity.class);
                startActivity(intent);
            }
        });
    }
}
