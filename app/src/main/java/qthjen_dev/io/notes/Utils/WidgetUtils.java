package qthjen_dev.io.notes.Utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class WidgetUtils {

    public static void setupSnackBar(Activity activity,View rootview, int description, Snackbar snackbar, int color) {
        snackbar = Snackbar.make(rootview, activity.getResources().getString(description), Snackbar.LENGTH_LONG);
        View v = snackbar.getView();
        TextView tv = v.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(activity.getResources().getColor(color));
        snackbar.show();
    }

    public static void hideKeyboard(Activity activity) {
        View v = activity.getWindow().getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
