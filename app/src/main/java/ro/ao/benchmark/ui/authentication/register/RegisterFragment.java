package ro.ao.benchmark.ui.authentication.register;

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

public class RegisterFragment extends BaseFragment {

    private View rootView; // Root view
    private Button registerButton;
    private TextView loginTextView;
    private ProgressBar progressBar;
    private TextInputEditText emailEditText, fullnameEditText, passwordEditText;

    private RegisterViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Constants.TAG_AUTHENTICATION, "ON REGISTER");

        /* Views */
        rootView = view;
        config();

        /* ViewModel */
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        observeViewModel();
    }

    private void config() {
        progressBar = rootView.findViewById(R.id.progressBar);
        emailEditText = rootView.findViewById(R.id.emailEditText);
        fullnameEditText = rootView.findViewById(R.id.fullnameEditText);
        passwordEditText = rootView.findViewById(R.id.passwordEditText);
        registerButton = rootView.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String fullname = fullnameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            register(email, fullname, password);
        });
        loginTextView = rootView.findViewById(R.id.loginTextView);
        loginTextView.setOnClickListener(v -> {
            ((AuthenticationActivity) getActivity()).changePage(0);
        });
    }

    private void register(String email, String fullname, String password) {
        OSUtil.hideKeyboard(getActivity());

        if (SanitizeUtil.isNullOrEmpty(email)
                || SanitizeUtil.isNullOrEmpty(fullname)
                || SanitizeUtil.isNullOrEmpty(password)) {
            Snackbar.make(rootView, R.string.complete_fields, Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (!SanitizeUtil.isEmailValid(email)) {
            Snackbar.make(rootView, R.string.invalid_email, Snackbar.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        viewModel.register(email, fullname, password);
    }

    private void observeViewModel() {
        viewModel.getExceptionLiveData().observe(getViewLifecycleOwner(), e -> {
            progressBar.setVisibility(View.GONE);
            displayAlert(getString(R.string.registration_failed), e.getMessage());
        });

        viewModel.getAccountLiveData().observe(getViewLifecycleOwner(), user -> {
            Log.d(Constants.TAG_AUTHENTICATION, "Register succeeded: " + user);

            progressBar.setVisibility(View.GONE);
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        });
    }
}
