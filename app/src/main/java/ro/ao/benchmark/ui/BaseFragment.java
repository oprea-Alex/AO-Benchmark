package ro.ao.benchmark.ui;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import ro.ao.benchmark.R;

public class BaseFragment extends Fragment {

    public void displayAlert(String title, String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.OK, null)
                .show();
    }

    public void displayAlert(String title, String message, DialogInterface.OnClickListener onPositiveCallback) {
        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.OK,  onPositiveCallback)
                .show();
    }
}
