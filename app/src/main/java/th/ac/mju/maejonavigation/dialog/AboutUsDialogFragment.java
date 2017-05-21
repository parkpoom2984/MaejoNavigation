package th.ac.mju.maejonavigation.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Teh on 5/21/2017.
 */

public class AboutUsDialogFragment extends SimpleTextDialogFragment{

    @Override
    protected CharSequence getTitle() {
        return "About us";
    }

    @Override
    protected CharSequence getMessage() {
        return "Information Technology Division,"+System.getProperty ("line.separator")+"Faculty of Science,"+System.getProperty ("line.separator")+"Maejo University";
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
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://page/720196448059091")); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/IT.Major.MJU")); //catches and opens a url to the desired page
        }
    }
}
