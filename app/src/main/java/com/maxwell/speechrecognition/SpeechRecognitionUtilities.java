package com.maxwell.speechrecognition;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.speech.RecognizerIntent;

import java.util.List;

/**
 * Created by Maxwell on 14-Jan-18.
 */

final class SpeechRecognitionUtilities {

    protected static boolean isInternetEnabled(Context context)
    {
        boolean isWifiConnected = false;
        boolean isMobileConnected = false;

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                if (networkInfo.isConnected())
                    isWifiConnected = true;
            }

            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                if (networkInfo.isConnected())
                    isMobileConnected = true;
            }

        }catch (Exception e){ }

        return isWifiConnected || isMobileConnected;
    }

    protected static boolean isSpeechRecognitionEnabled(Context context){

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0)
            return false;

        return true;
    }
}
