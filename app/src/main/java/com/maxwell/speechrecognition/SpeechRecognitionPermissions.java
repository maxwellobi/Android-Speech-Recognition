package com.maxwell.speechrecognition;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Maxwell on 14-Jan-18.
 */

public class SpeechRecognitionPermissions extends Fragment {

    private static final int PERMISSION_REQUEST_AUDIO = 123;
    private OnSpeechRecognitionPermissionListener onSpeechRecognitionPermissionListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    void setSpeechRecognitionPermissionListener(@NonNull OnSpeechRecognitionPermissionListener onSpeechRecognitionPermissionListener){
        this.onSpeechRecognitionPermissionListener = onSpeechRecognitionPermissionListener;
    }

    void requestPermissions(){

        /*
          * Using requestPermissions from Marshmallow and above (>= API 23)
          * Below Marshmallow, the app will automatically request
          * for the permission in manifest when installed
          */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_AUDIO);
    }

    Boolean isPermissionGiven(@NonNull Context context){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;

        return true; //permission already request on install if < API 23
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_REQUEST_AUDIO){

            // If request is granted, the result arrays are not empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (onSpeechRecognitionPermissionListener != null) {
                    onSpeechRecognitionPermissionListener.onPermissionGranted();
                }
                return;
            }
        }
        if (onSpeechRecognitionPermissionListener != null) {
            onSpeechRecognitionPermissionListener.onPermissionDenied();
        }
    }
}
