package com.maxwell.speechrecognition;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by Maxwell on 14-Jan-18.
 */

public class SpeechRecognitionPermissions extends Fragment {

    private static final int PERMISSION_REQUEST_AUDIO = 123400;
    private Boolean permissionGiven = false;
    private OnSpeechRecognitionPermissionListener onSpeechRecognitionPermissionListener;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        /**
         * The OnSpeechRecognitionPermissionListener is implemented
         * in client (the context instance using SpeechRecognition).
         * So getActivity() will return the clients class (context)
         * that implements this Permission Listener
         */
        this.onSpeechRecognitionPermissionListener = (OnSpeechRecognitionPermissionListener)getActivity();
    }

    public void requestPermissions(Context context){

        /*
          * Using ContextCompat because of backwards compatibility
          * Context.checkSelfPermission requires API level 23, min is 19
         */
        if (!isPermissionGiven(context)) {
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSION_REQUEST_AUDIO);
        }

    }

    public Boolean isPermissionGiven(Context context){
        return ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        if(requestCode == PERMISSION_REQUEST_AUDIO){

            // If request is granted, the result arrays are not empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onSpeechRecognitionPermissionListener.onPermissionGranted();
                return;
            }

            onSpeechRecognitionPermissionListener.onPermissionDenied();
        }
    }
}
