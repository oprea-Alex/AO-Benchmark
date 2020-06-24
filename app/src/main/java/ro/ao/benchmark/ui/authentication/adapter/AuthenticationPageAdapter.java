package ro.ao.benchmark.ui.authentication.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import ro.ao.benchmark.R;
import ro.ao.benchmark.ui.authentication.login.LoginFragment;
import ro.ao.benchmark.ui.authentication.register.RegisterFragment;

public class AuthenticationPageAdapter extends FragmentPagerAdapter {

    private Context context;

    public AuthenticationPageAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LoginFragment();
            case 1:
                return new RegisterFragment();
            default:
                return null; /* Invalid position */
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.login);
            case 1:
                return context.getString(R.string.register);
            default:
                return "";
        }
    }

    @Override
    public int getCount() {
        return 2; /* Two pages: Login and Register */
    }
}
