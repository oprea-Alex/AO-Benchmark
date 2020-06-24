package ro.ao.benchmark.ui.main.account;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ro.ao.benchmark.model.account.UserInfo;
import ro.ao.benchmark.ui.BaseViewModel;
import ro.ao.benchmark.util.Constants;

public class AccountViewModel extends BaseViewModel {

    /* User live data */
    protected MutableLiveData<UserInfo> userInfoLiveData;

    public AccountViewModel(Application app) {
        super(app);
        userInfoLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<UserInfo> getUserInfoLiveData() {
        return userInfoLiveData;
    }

    public void getUserInfo() {

        FirebaseUser account = FirebaseAuth.getInstance().getCurrentUser();
        if (account == null) {
            exceptionLiveData.postValue(new Exception("Failed to retrieve user information"));
            return;
        }

        FirebaseDatabase.getInstance()
                .getReference(Constants.USERS_TABLE)
                .child(account.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        /* Cast result to user info */
                        UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);

                        /* Update UI */
                        userInfoLiveData.postValue(userInfo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        exceptionLiveData.postValue(databaseError.toException());
                    }
                });
    }
}