package ro.ao.benchmark.ui.authentication.register;

import android.app.Application;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import ro.ao.benchmark.model.account.UserInfo;
import ro.ao.benchmark.ui.BaseViewModel;
import ro.ao.benchmark.util.Constants;
import ro.ao.benchmark.util.CryptoUtil;

public class RegisterViewModel extends BaseViewModel {

    public RegisterViewModel(Application app) {
        super(app);
    }

    public void register(String email, String fullname, String password) {
        Log.d(Constants.TAG_AUTHENTICATION, "Perform register for: " + email);

        /* Check password */
        try {
            final String encryptedPassword = CryptoUtil.encryptMessage(password);

            /* Perform user registration */
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();

                            /* Save user info in Firebase RT Database */
                            updateRemoteDB(user.getUid(), email, fullname, encryptedPassword);

                            /* Update UI */
                            accountLiveData.postValue(user);
                        } else {
                            Log.e(Constants.TAG_AUTHENTICATION, "Register failed", task.getException());
                            exceptionLiveData.postValue(task.getException());
                        }
                    });
        } catch (Exception e) {
            Log.e(Constants.TAG_AUTHENTICATION, "Register failed", e);
            exceptionLiveData.postValue(e);
        }
    }

    private void updateRemoteDB(String id, String email, String fullname, String encryptedPassword) {
        UserInfo user = new UserInfo(
                id,
                fullname,
                email,
                encryptedPassword
        );

        FirebaseDatabase.getInstance()
                .getReference(Constants.USERS_TABLE)
                .child(id)
                .setValue(user);
    }
}