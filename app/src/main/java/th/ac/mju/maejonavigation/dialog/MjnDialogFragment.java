package th.ac.mju.maejonavigation.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.widget.Button;

import th.ac.mju.maejonavigation.R;

/**
 * Created by Teh on 5/21/2017.
 */

abstract class MjnDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = onCreateBuilder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        onConfigBuilder(builder, inflater);
        Dialog dialog = onCreateDialog(builder);
        onConfigDialog(dialog);
        return dialog;
    }

    @NonNull
    protected AlertDialog.Builder onCreateBuilder(Context context) {
        return new AlertDialog.Builder(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        Button positive = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
        Button negative = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE);
        Button neutral = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEUTRAL);
        positive.setTextColor(ContextCompat.getColor(getContext(), R.color.mjn_primary));
        negative.setTextColor(ContextCompat.getColor(getContext(), R.color.mjn_primary));
        neutral.setTextColor(ContextCompat.getColor(getContext(), R.color.mjn_primary));
        positive.setTextSize(10);
        negative.setTextSize(10);
        neutral.setTextSize(10);
    }

    protected abstract void onConfigBuilder(
            AlertDialog.Builder builder,
            LayoutInflater inflater);

    protected Dialog onCreateDialog(AlertDialog.Builder builder) {
        return builder.create();
    }

    protected void onConfigDialog(Dialog dialog) {}
}
