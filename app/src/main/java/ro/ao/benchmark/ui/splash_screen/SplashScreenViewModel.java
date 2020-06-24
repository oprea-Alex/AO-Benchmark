package ro.ao.benchmark.ui.splash_screen;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;

import ro.ao.benchmark.ui.BaseViewModel;

public class SplashScreenViewModel extends BaseViewModel {
    private MutableLiveData<Boolean> userIsLoggedInLiveData;

    public SplashScreenViewModel(Application app) {
        super(app);
        userIsLoggedInLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getUserIsLoggedInLiveData() {
        return userIsLoggedInLiveData;
    }

    public void checkUserIsLoggedIn() {
        userIsLoggedInLiveData.postValue(
                FirebaseAuth.getInstance().getCurrentUser() != null
        );
    }

}
