package th.ac.mju.maejonavigation.dialog;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.google.common.base.Optional;

/**
 * Created by Teh on 5/21/2017.
 */

public class Dialogs {
    public static void show(FragmentActivity activity, DialogFragment dialog) {
        show(activity.getSupportFragmentManager(), dialog);
    }

    private static void show(FragmentManager fm, DialogFragment dialog) {
        final String tag = dialog.getTag() != null ? dialog.getTag() : dialog.getClass().getName();
        Optional<DialogFragment> old = find(fm, tag);
        if (!old.isPresent()) {
            dialog.show(fm, tag);
        }
    }

    private static Optional<DialogFragment> find(FragmentManager fm, String tag) {
        Optional<DialogFragment> result = Optional.absent();
        Fragment f = fm.findFragmentByTag(tag);
        if (f instanceof DialogFragment) {
            result = Optional.of((DialogFragment) f);
        }
        return result;
    }
}
