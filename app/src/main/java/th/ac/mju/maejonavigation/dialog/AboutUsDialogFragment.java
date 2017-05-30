package th.ac.mju.maejonavigation.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;

/**
 * Created by Teh on 5/21/2017.
 */

public class AboutUsDialogFragment extends SimpleTextDialogFragment {
    @Override
    protected void onConfigBuilder(AlertDialog.Builder builder, LayoutInflater inflater) {
        super.onConfigBuilder(builder, inflater);
        builder.setIcon(null);
    }

    @Override
    protected CharSequence getTitle() {
        return "About us";
    }

    @Override
    protected CharSequence getMessage() {
        return "Information Technology Division," +
                System.getProperty("line.separator") +
                "Faculty of Science," +
                System.getProperty("line.separator") +
                "Maejo University";
    }

    @Override
    protected void onNegativeButtonClick(DialogInterface dialog, int which) {
        super.onNegativeButtonClick(dialog, which);
    }

    @Override
    protected CharSequence getPositiveText() {
        return "CONTACT";
    }

    @Override
    protected CharSequence getNegativeText() {
        return "CLOSE";
    }

    @Override
    protected void onPositiveButtonClick(DialogInterface dialog, int which) {
        super.onPositiveButtonClick(dialog, which);
        startActivity(getOpenFacebookIntent(getContext()));
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
