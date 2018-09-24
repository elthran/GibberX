/*
package elthran.gibberx;

import android.util.Log;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.*;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler;

import java.util.List;

public class AppHelper {
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
            String current_level = cognitoUserDetails.getAttributes().getAttributes().get("custom:level");
            Log.e("getDetailsHandler", "success");
            Log.e("getDetailsHandler", current_level);
        }

        @Override
        public void onFailure(Exception exception) {
            // Fetch user details failed, check exception for the cause
            Log.e("getDetailsHandler", "failure");
        }
    };
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
    // Fetch the user details
        cognitoUser.getDetailsInBackground(getDetailsHandler);
    // Create an empty attribute class
    CognitoUserAttributes my_attributes = new CognitoUserAttributes();
    // Add attributes to be changed to the class
        my_attributes.addAttribute("custom:level", "5");
    // Update current user with the new attributes
    cognitoUser.updateAttributesInBackground(my_attributes, updateAttributesHandler);
}
*/
