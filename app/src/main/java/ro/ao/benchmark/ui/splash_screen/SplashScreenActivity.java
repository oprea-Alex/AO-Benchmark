package ro.ao.benchmark.ui.splash_screen;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import ro.ao.benchmark.R;
import ro.ao.benchmark.ui.authentication.AuthenticationActivity;
import ro.ao.benchmark.ui.main.MainActivity;
import ro.ao.benchmark.util.Constants;

public class SplashScreenActivity extends AppCompatActivity {

    /* Permissions request code */
    private final int STORAGE_REQ_CODE = 1230;

    private SplashScreenViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        viewModel = ViewModelProviders.of(this).get(SplashScreenViewModel.class);
        observeViewModel();
        requestPermissions();
    }

    private void requestPermissions() {
        String[] permissionsToCheck = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        int cnt = 0;
        String[] permissionsToAsk = new String[2];

        for (String permission : permissionsToCheck) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToAsk[cnt ++] = permission;
            }
        }

        requestPermissions(permissionsToAsk, STORAGE_REQ_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case STORAGE_REQ_CODE:
                Log.d(Constants.TAG_DEBUG, "On permissions result");
                viewModel.checkUserIsLoggedIn();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void observeViewModel() {
        viewModel.getUserIsLoggedInLiveData().observe(this, userIsLoggedIn -> {
            launchApp(userIsLoggedIn ? MainActivity.class : AuthenticationActivity.class);
        });
    }

    private void launchApp(Class destination) {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, destination));
            finish();
        }, Constants.SPLASH_SCREEN_DURATION);
    }

}
