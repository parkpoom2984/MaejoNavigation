package th.ac.mju.maejonavigation.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;

import th.ac.mju.maejonavigation.R;

/**
 * Created by Teh on 5/21/2017.
 */

abstract class SimpleTextDialogFragment extends MjnDialogFragment {
    @Override
    protected void onConfigBuilder(AlertDialog.Builder builder, LayoutInflater inflater) {
        builder.setIcon(getIconResId());
        builder.setTitle(getTitle());
        builder.setMessage(getMessage());

        builder.setNegativeButton(getNegativeText(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onNegativeButtonClick(dialog, which);
            }
        });
        builder.setPositiveButton(getPositiveText(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onPositiveButtonClick(dialog, which);
            }
        });
    }

    protected int getIconResId() {
        return R.mipmap.ic_launcher;
    }

    protected abstract CharSequence getTitle();

    protected abstract CharSequence getMessage();

    protected CharSequence getNegativeText() {
        return null;
    }

    protected CharSequence getPositiveText() {
        return getString(android.R.string.ok);
    }

    protected void onNegativeButtonClick(DialogInterface dialog, int which) {}

    protected void onPositiveButtonClick(DialogInterface dialog, int which) {}
}
