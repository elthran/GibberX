package elthran.gibberx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.cognitoidentityprovider.AmazonCognitoIdentityProviderClient;
import com.amazonaws.services.cognitoidentityprovider.model.ListUsersRequest;
import com.amazonaws.services.cognitoidentityprovider.model.ListUsersResult;
import com.amazonaws.services.cognitoidentityprovider.model.UserType;

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
        //String currentUserName = AWSMobileClient.defaultMobileClient().getIdentityManager().getIdentityProfile().getUserName();
        //userName.setText(currentUserName);
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
                        userName.setText(s);
                    }

                    @Override
                    public void handleError(Exception e) {
                        Log.e("MainActivity", "Error in retrieving Identity ID: " + e.getMessage());
                    }
                });
            }
        }).execute();

        // Create log out Button on click listener
        logout_button.setOnClickListener( new View.OnClickListener() {

            public void onClick(View v) {
                IdentityManager.getDefaultIdentityManager().signOut();
                Intent intent = new Intent(MainActivity.this, AuthenticatorActivity.class);
                startActivity(intent);
            }
        });

        CognitoUserPool userPool = new CognitoUserPool(this, "us-east-1_E6Jjn1DY6", "26c1jmeli9go3gvc80pj0ld5bt",
                "1oajk661no1dvs98a1dhetd5kmdsbftkcjjisv889ilta66npo4v");
        CognitoUser cognitoUser = userPool.getCurrentUser();
        Log.e("Print_User", String.valueOf(cognitoUser));
    }
}
