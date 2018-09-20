package th.ac.mju.maejonavigation.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;

import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.screen.privacy_policy.PrivacyPolicyActivity;

/**
 * Created by Teh on 5/21/2017.
 */

public class AboutUsDialogFragment extends SimpleTextDialogFragment {
    private static final String DIVISION = "Information Technology Division,";
    private static final String FACULTY = "Faculty of Science,";
    private static final String UNIVERSITY = "Maejo University";

    @Override
    protected void onConfigBuilder(AlertDialog.Builder builder, LayoutInflater inflater) {
        super.onConfigBuilder(builder, inflater);
        builder.setIcon(null);
    }

    @Override
    protected CharSequence getTitle() {
        return getString(R.string.about_us);
    }

    @Override
    protected CharSequence getMessage() {
        return DIVISION +
                System.getProperty("line.separator") +
                FACULTY +
                System.getProperty("line.separator") +
                UNIVERSITY;
    }

    @Override
    protected void onNegativeButtonClick(DialogInterface dialog, int which) {
        super.onNegativeButtonClick(dialog, which);
    }

    @Override
    protected void onPositiveButtonClick(DialogInterface dialog, int which) {
        super.onPositiveButtonClick(dialog, which);
        startActivity(getOpenFacebookIntent(getContext()));
    }

    @Override
    protected void onNeutralButtonClick(DialogInterface dialog, int which) {
        super.onNeutralButtonClick(dialog, which);
        Intent intent = new Intent(getContext(), PrivacyPolicyActivity.class);
        startActivity(intent);
    }

    @Override
    protected CharSequence getPositiveText() {
        return getString(R.string.contact);
    }

    @Override
    protected CharSequence getNegativeText() {
        return getString(R.string.close);
    }

    @Override
    protected CharSequence getNeutralText() {
        return getString(R.string.privacy_policy);
    }

    private static Intent getOpenFacebookIntent(Context context) {
        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://page/720196448059091"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse(
                            "https://www.facebook.com/IT.Major.MJU"));
        }
    }
}
