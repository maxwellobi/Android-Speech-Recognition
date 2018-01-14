package com.maxwell.speechrecognition;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;

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
    private boolean enableOnlyOfflineRecognition = false;
    private boolean requestPermission = true;

    /**
     * SpeechRecognition Constructor
     */
    public SpeechRecognition(){}

    /**
     * Initialize the SpeechRecognition class with the
     * application context instance requesting speech recognition
     * @param  context  the application context (cannot be null)
     * @see    Context
     */
    public SpeechRecognition(Context context){
        this.context = context;
    }

    /**
     * Sets the application context instance requesting speech recognition
     * @param  context  the application context (cannot be null)
     * @see    Context
     */
    public void setContext(@NonNull Context context){
        this.context = context;
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

    public void startSpeechRecognition(){
        //TODO: check if speech recognition is enabled on this phone
    }

    /**
     * Sets SpeechRecognition to use only offline recognition engine
     * Disabled by default - meaning either internet or offline recognition engines may be used
     * @param  onlyOfflineRecognition  true or false (false by default)
     * @since API level 23
     */
    public void useOnlyOfflineRecognition(boolean onlyOfflineRecognition){
        this.enableOnlyOfflineRecognition = onlyOfflineRecognition;
    }

    private void initialize(){

        /*
         *Initialize the SpeechRecognizer and set listener with user implement onSpeechRecognizerListener
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
