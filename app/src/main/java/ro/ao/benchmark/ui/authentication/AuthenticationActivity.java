package ro.ao.benchmark.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

import ro.ao.benchmark.R;
import ro.ao.benchmark.ui.authentication.adapter.AuthenticationPageAdapter;
import ro.ao.benchmark.util.Constants;

public class AuthenticationActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private AuthenticationPageAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        Log.d(Constants.TAG_AUTHENTICATION, "ON CREATE");

        setTitle(R.string.authentication);

        /* Setup containers (login and register) */
        viewPager = findViewById(R.id.authenticationViewPager);
        adapter = new AuthenticationPageAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        /* Setup toolbar  */
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    /* Change current page */
    public void changePage(int index) {
        viewPager.setCurrentItem(index);
    }
}
