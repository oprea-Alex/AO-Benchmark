package ro.ao.benchmark.ui.main.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import ro.ao.benchmark.R;
import ro.ao.benchmark.databinding.FragmentAccountBinding;
import ro.ao.benchmark.ui.BaseFragment;
import ro.ao.benchmark.ui.authentication.AuthenticationActivity;
import ro.ao.benchmark.util.Constants;
import ro.ao.benchmark.util.OSUtil;

public class AccountFragment extends BaseFragment {

    private AccountViewModel viewModel;

    private FragmentAccountBinding binding;

    private View rootView;
    private ProgressBar progressBar;
    private Button signOutButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_account,
                container,
                false
        );

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Constants.TAG_USER_PROFILE, "ON CREATE");

        /* Views */
        rootView = view;
        config();

        /* ViewModel */
        viewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        observeViewModel();

        retrieveUserInfo();
    }

    private void config() {
        progressBar = rootView.findViewById(R.id.progressBar);
        signOutButton = rootView.findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(v -> {
            signOut();
        });
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void retrieveUserInfo() {
        progressBar.setVisibility(View.VISIBLE);
        viewModel.getUserInfo();
    }

    private void observeViewModel() {
        viewModel.getExceptionLiveData().observe(getViewLifecycleOwner(), e -> {
            progressBar.setVisibility(View.GONE);
            displayAlert(getString(R.string.retrieve_user_info_failed), e.getMessage());
        });
        viewModel.getUserInfoLiveData().observe(getViewLifecycleOwner(), acc -> {
            Log.d(Constants.TAG_USER_PROFILE, "User info retrieved: " + acc.getEmail());

            /* Display user info */
            progressBar.setVisibility(View.GONE);
            binding.setAccount(acc);
            ((TextInputEditText) rootView.findViewById(R.id.deviceModelEditText))
                    .setText(OSUtil.getDeviceName());
        });
    }
}
