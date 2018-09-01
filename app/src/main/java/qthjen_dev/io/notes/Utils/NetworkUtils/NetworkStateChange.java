package qthjen_dev.io.notes.Utils.NetworkUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;

public class NetworkStateChange extends BroadcastReceiver {

    public static final String NETWORK_AVAILABLE_ACTION = "io.qthjen_dev.NetworkAvailable";
    public static final String IS_NETWORK_AVAILABLE = "isNetworkAvailable";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent networkState = new Intent(NETWORK_AVAILABLE_ACTION);
        networkState.putExtra(IS_NETWORK_AVAILABLE, CheckNetwork(context));
        LocalBroadcastManager.getInstance(context).sendBroadcast(networkState);
    }

    public static boolean CheckNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}