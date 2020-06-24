package ro.ao.benchmark.ui.authentication.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import ro.ao.benchmark.R;
import ro.ao.benchmark.ui.BaseFragment;
import ro.ao.benchmark.ui.authentication.AuthenticationActivity;
import ro.ao.benchmark.ui.main.MainActivity;
import ro.ao.benchmark.util.Constants;
import ro.ao.benchmark.util.OSUtil;
import ro.ao.benchmark.util.SanitizeUtil;

public class LoginFragment extends BaseFragment {

    private View rootView;
    private Button loginButton;
    private TextView registerTextView;
    private ProgressBar progressBar;
    private TextInputEditText emailEditText, passwordEditText;

    private LoginViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Constants.TAG_AUTHENTICATION, "ON LOGIN");

        /* Views */
        rootView = view;
        config();

        /* ViewModel */
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        observeViewModel();
    }

    private void config() {
        progressBar = rootView.findViewById(R.id.progressBar);
        loginButton = rootView.findViewById(R.id.loginButton);
        emailEditText = rootView.findViewById(R.id.emailEditText);
        passwordEditText = rootView.findViewById(R.id.passwordEditText);
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            login(email, password);
        });
        registerTextView = rootView.findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(v -> {
            ((AuthenticationActivity) getActivity()).changePage(1);
        });
    }


    private void login(String email, String password) {
        OSUtil.hideKeyboard(getActivity());

        if (SanitizeUtil.isNullOrEmpty(email) || SanitizeUtil.isNullOrEmpty(password)) {
            Snackbar.make(rootView, R.string.complete_fields, Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (!SanitizeUtil.isEmailValid(email)) {
            Snackbar.make(rootView, R.string.invalid_email, Snackbar.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        viewModel.login(email, password);
    }

    private void observeViewModel() {
        viewModel.getExceptionLiveData().observe(getViewLifecycleOwner(), e -> {
            progressBar.setVisibility(View.GONE);
            displayAlert(getString(R.string.login_failed), e.getMessage());
        });

        viewModel.getAccountLiveData().observe(getViewLifecycleOwner(), user -> {
            Log.d(Constants.TAG_AUTHENTICATION, "Login Succeeded: " + user);
            progressBar.setVisibility(View.GONE);
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        });
    }
}
