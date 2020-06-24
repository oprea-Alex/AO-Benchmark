package ro.ao.benchmark.ui.authentication.login;

import android.app.Application;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import ro.ao.benchmark.ui.BaseViewModel;
import ro.ao.benchmark.util.Constants;

public class LoginViewModel extends BaseViewModel {

    public LoginViewModel(Application app) {
        super(app);
    }

    public void login(String email, String password) {
        Log.d(Constants.TAG_AUTHENTICATION, "Perform login for" + email);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                /* Update UI */
                accountLiveData.postValue(auth.getCurrentUser());
            } else {
                Log.e(Constants.TAG_AUTHENTICATION, "Login failed", task.getException());
                exceptionLiveData.postValue(task.getException());
            }
        });
    }
}