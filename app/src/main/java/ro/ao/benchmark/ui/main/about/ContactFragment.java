package ro.ao.benchmark.ui.main.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ro.ao.benchmark.R;
import ro.ao.benchmark.ui.BaseFragment;

public class ContactFragment extends BaseFragment {

    private View rootView;
    private TextView callUsTextView, emailUsTextView, colaborateTextView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = view;
        config();
    }

    private void config() {
        callUsTextView = rootView.findViewById(R.id.callUsTextView);
        emailUsTextView = rootView.findViewById(R.id.emailUsTextView);
        colaborateTextView = rootView.findViewById(R.id.colaborateTextView);

        callUsTextView.setOnClickListener(v -> {
            performCall();
        });
        emailUsTextView.setOnClickListener(v -> {
            sendEmail();
        });
        colaborateTextView.setOnClickListener(v -> {
            collaborate();
        });
    }

    private void performCall() {
        String phone = "+40722821183";
        Intent callIntent = new Intent(
                Intent.ACTION_DIAL,
                Uri.fromParts("tel", phone, null)
        );
        startActivity(callIntent);
    }

    private void sendEmail() {
        Intent emailIntent = new Intent(
                Intent.ACTION_SENDTO,
                Uri.fromParts("mailto","oprea.alexandru199@gmail.com", null)
        );
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "AO-BenchMark");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Greetings!");

        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void collaborate() {
        Intent browserIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.linkedin.com/in/alexandru-oprea-3507a7177/")
        );
        startActivity(browserIntent);
    }
}
