package com.maxwell.speechrecognition;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Maxwell on 13-Jan-18.
 */

public class SpeechRecognition {

    private final int REQUEST_CODE = -123400;
    private final int MAX_RESULT_COUNT = 3;

    private Context context;
    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;

    private OnSpeechRecognitionListener onSpeechRecognitionListener;
    private SpeechRecognitionPermissions speechRecognitionPermissions;

    private boolean enableOnlyOfflineRecognition = false;
    private boolean handlePermissions = true;

    /**
     * Initialize the SpeechRecognition class with the
     * application context instance requesting SpeechRecognition.
     * It is recommended you initialize this class in your Constructor
     * or in <strong>onCreate</strong> of your Activity.
     *
     * @param  context  the application context (cannot be null)
     * @see    Context
     */
    public SpeechRecognition(Context context){
        this.context = context;
        initialize();
    }

    /**
     * Sets the application context instance requesting SpeechRecognition
     *
     * @param  context  the application context (cannot be null)
     * @see    Context
     */
    public void setContext(@NonNull Context context){
        this.context = context;
        initialize();
    }

    /**
     * Sets the {@link OnSpeechRecognitionListener} that will receive all the callbacks
     * for handling the SpeechRecognition responses or Errors
     * @param  onSpeechRecognitionListener  the listener that will receive the callbacks.
     * @see    OnSpeechRecognitionListener
     */
    public void setListener(@NonNull OnSpeechRecognitionListener onSpeechRecognitionListener){
        this.onSpeechRecognitionListener = onSpeechRecognitionListener;
    }

    public void setPreferredLanguage(){
        throw new UnsupportedOperationException();
    }

    /**
     * By default, {@link SpeechRecognition} requests for Audio permissions and returns the result
     * through {@link OnSpeechRecognitionPermissionListener} which is implemented in your Activity/Context.
     * You can decide to handle Audio permissions yourself by setting handleAudioPermissions to false.
     *
     * @param  handlePermissions  true or false whether to handle audio permissions yourself or not.
     * @see    OnSpeechRecognitionPermissionListener
     */
    public void handleAudioPermissions(boolean handlePermissions){
        this.handlePermissions = handlePermissions;
    }

    public void startSpeechRecognition(){
        //TODO: check if speech recognition is enabled on this phone

        //Permission is not given yet, and handlePermission is false, throw error
        if(!speechRecognitionPermissions.isPermissionGiven(context)){
            if(!handlePermissions)
                throw new SecurityException(context.getString(R.string.security_exception_text));

            speechRecognitionPermissions.requestPermissions();
        }

    }

    /**
     * Sets {@link SpeechRecognition} to use only it's offline recognition engine
     * This is disabled by default - meaning that either internet or offline recognition
     * engines may be used at anytime. <strong>It is highly recommended that this is set to false</strong>
     *
     * @param  onlyOfflineRecognition  true or false whether to use only offline recognition or not (false by default)
     * @since API level 23
     */
    public void useOnlyOfflineRecognition(boolean onlyOfflineRecognition){
        this.enableOnlyOfflineRecognition = onlyOfflineRecognition;
    }

    private void initialize(){

         /*
          * Initialize the SpeechRecognitionPermissions and set listener with
          * OnSpeechRecognitionPermissionListener implemented by client
         */
        speechRecognitionPermissions = new SpeechRecognitionPermissions();
        ((Activity) context).getFragmentManager()
                .beginTransaction()
                .add(speechRecognitionPermissions, SpeechRecognition.class.getSimpleName())
                .commit();

        OnSpeechRecognitionPermissionListener permissionListener = (OnSpeechRecognitionPermissionListener)context;
        speechRecognitionPermissions.setSpeechRecognitionPermissionListener(permissionListener);

        /*
         *Initialize the SpeechRecognizer and set listener with onSpeechRecognizerListener implemented by client
         */
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(new SpeechRecognitionListener(onSpeechRecognitionListener));

        /*
         *Initialize the Speech recognition intent with default Language
         */
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, MAX_RESULT_COUNT);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        /*
         * Only offline recognition works from API level 23
         */
        if(enableOnlyOfflineRecognition){
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
        }

        //TODO: Set preferred Speech recognition Language
    }


}
