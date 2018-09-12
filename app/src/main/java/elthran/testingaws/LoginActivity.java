package elthran.testingaws;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.services.cognitoidentityprovider.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidentityprovider.AWSCognitoIdentityProviderClientBuilder;

public class LoginActivity extends AppCompatActivity {

    CognitoUserPool userPool = new CognitoUserPool(context, userPoolId, clientId, clientSecret);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
