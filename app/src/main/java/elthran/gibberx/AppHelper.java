package elthran.gibberx;

import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler;

import java.util.List;

class CustomUpdateAttributesHandler implements UpdateAttributesHandler {
    @Override
    public void onSuccess(List<CognitoUserCodeDeliveryDetails> attributesVerificationList) {
        Log.e("updateAttributesHandler", "success");
    }
    @Override
    public void onFailure(Exception exception) {
        Log.e("updateAttributesHandler", "failure");
    }
}

class PermissionsDetailsHandler implements GetDetailsHandler {
    private CognitoUser cognitoUser;
    PermissionsDetailsHandler(CognitoUser cognitoUser) {
        this.cognitoUser = cognitoUser;
    }
    @Override
    public void onSuccess(CognitoUserDetails cognitoUserDetails) {
        // Create an empty attribute class
        CognitoUserAttributes checkPermissions = new CognitoUserAttributes();
        final String booksUnlocked = checkPermissions.getAttributes().get("custom:books_unlocked");
        final String wordsUnlocked = checkPermissions.getAttributes().get("custom:words_unlocked");
        // Add attributes to be changed to the class
        if ((booksUnlocked == null) || (Integer.parseInt(booksUnlocked) < 1)) {
            checkPermissions.addAttribute("custom:books_unlocked", "1");
            Log.e("checkLevelHandler-books", "Attribute books_unlocked being set to 1.");
        } else {
            Log.e("checkLevelHandler-words", "Attribute books_unlocked passed test");
        }
        if ((wordsUnlocked == null) || (Integer.parseInt(wordsUnlocked) < 1)) {
            checkPermissions.addAttribute("custom:words_unlocked", "1");
            Log.e("checkLevelHandler", "Attribute words_unlocked being set to 1.");
        } else {
            Log.e("checkLevelHandler", "Attribute words_unlocked passed test");
        }
        CustomUpdateAttributesHandler customUpdateAttributesHandler = new CustomUpdateAttributesHandler();
        cognitoUser.updateAttributesInBackground(checkPermissions, customUpdateAttributesHandler);
    }
    @Override
    public void onFailure(Exception exception) {
        // Fetch user details failed, check exception for the cause
        Log.e("checkLevelHandler", "failure");
        Log.e("checkLevelHandler", String.valueOf(exception));
    }
}