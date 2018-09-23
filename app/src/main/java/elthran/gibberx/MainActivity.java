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
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;


import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create the logout button
        Button logout_button = (Button) findViewById(R.id.button_logout);
        // Create the user display
        TextView userName = (TextView) findViewById(R.id.userDisplay);
        // Get the current user pool
        CognitoUserPool userPool = new CognitoUserPool(this, IdentityManager.getDefaultIdentityManager().getConfiguration());
        // Find the user from the user pool
        CognitoUser cognitoUser = userPool.getCurrentUser();
        // Get the user's username
        String current_username = cognitoUser.getUserId();




        // Implement callback handler for getting details
        GetDetailsHandler getDetailsHandler = new GetDetailsHandler() {
            @Override
            public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                // The user detail are in cognitoUserDetails
                Log.e("GetDetailsinBackground", "success");
                Log.e("DetailsList", String.valueOf(cognitoUserDetails));
            }
            @Override
            public void onFailure(Exception exception) {
                // Fetch user details failed, check exception for the cause
                Log.e("GetDetailsinBackground", "failure");
            }
        };
        // Fetch the user details
        cognitoUser.getDetailsInBackground(getDetailsHandler);


        CognitoUserAttributes my_attributes = new CognitoUserAttributes();
        my_attributes.addAttribute("gender", "male");
        UpdateAttributesHandler updateHandler = new UpdateAttributesHandler() {
            @Override
            public void onSuccess(List<CognitoUserCodeDeliveryDetails> attributesVerificationList) {
                Log.e("UpdateList", String.valueOf(attributesVerificationList));
                Log.e("Update", "success");
            }
            @Override
            public void onFailure(Exception exception) {
                Log.e("Update", "failure");
            }
        };
        cognitoUser.updateAttributesInBackground(my_attributes, updateHandler);

        Log.e("User Info", cognitoUser.toString());





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
                        Log.d("MainActivity", "Identity ID is: " + s);
                        Log.d("MainActivity", "Cached Identity ID: " + IdentityManager.getDefaultIdentityManager().getCachedUserID());
                        userName.setText(current_username);
                    }

                    @Override
                    public void handleError(Exception e) {
                        Log.e("MainActivity", "Error in retrieving Identity ID: " + e.getMessage());
                    }
                });
            }
        }).execute();

        // Create log out Button on click listener
        logout_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                IdentityManager.getDefaultIdentityManager().signOut();
                Intent intent = new Intent(MainActivity.this, AuthenticatorActivity.class);
                startActivity(intent);
            }
        });
    }
}
