package com.maxwell.speechrecognition;

/**
 * Created by Maxwell on 14-Jan-18.
 */

public interface OnSpeechRecognitionPermissionListener {

    void onPermissionGranted();

    void onPermissionDenied();
}
