package com.maxwell.speechrecognition;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Maxwell on 13-Jan-18.
 */

public class SpeechRecognition {

    static final int MAX_RESULT_COUNT = 3;

    private Context context;
    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;

    private OnSpeechRecognitionListener onSpeechRecognitionListener;
    private SpeechRecognitionPermissions speechRecognitionPermissions;
    private OnSpeechRecognitionPermissionListener onSpeechRecognitionPermissionListener;
    private GoogleImeSpeechRecognition googleImeSpeechRecognition;

    private boolean enableOnlyOfflineRecognition = false;
    private boolean handlePermissions = true;
    private boolean useGoogleIme = false;
    private String googleImePrompt = "Speech Recognition";

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
        initializeSpeechRecognitionParameters();
    }

    /**
     * Sets the application context instance requesting SpeechRecognition
     *
     * @param  context  the application context (cannot be null)
     * @see    Context
     */
    public void setContext(@NonNull Context context){
        this.context = context;
        initializeSpeechRecognitionParameters();
    }

    /**
     * Sets the {@link OnSpeechRecognitionListener} that will receive the callbacks
     * for handling the SpeechRecognition responses or Errors.
     *
     * @param  onSpeechRecognitionListener  the listener that will receive the callbacks.
     * @see    OnSpeechRecognitionListener
     */
    public void setSpeechRecognitionListener(@NonNull OnSpeechRecognitionListener onSpeechRecognitionListener){
        this.onSpeechRecognitionListener = onSpeechRecognitionListener;
    }

    /**
     * Sets the {@link OnSpeechRecognitionPermissionListener} that will receive the permission request callback.
     * This listener handles the onPermissionGranted and onPermissionDenied callbacks.
     * <strong>You must set this if {@link SpeechRecognition} is handling the permission request for you.</strong>
     *
     * @param  onSpeechRecognitionPermissionListener  the listener that will receive the permission callbacks.
     * @throws UnsupportedOperationException
     * @see    OnSpeechRecognitionPermissionListener
     * @see    #handleAudioPermissions(boolean)
     */
    public void setSpeechRecognitionPermissionListener(@NonNull OnSpeechRecognitionPermissionListener onSpeechRecognitionPermissionListener){

        if(!handlePermissions) throw new UnsupportedOperationException(context.getString(R.string.set_permission_listener_exception_text));
        this.onSpeechRecognitionPermissionListener = onSpeechRecognitionPermissionListener;
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
     * @see    #setContext(Context)
     */
    public void handleAudioPermissions(boolean handlePermissions){
        this.handlePermissions = handlePermissions;
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

    /**
     * This allows {@link SpeechRecognition} to use Google Voice Ime.
     * <strong>Note: This prevents you from doing Continuous Recognition</strong>
     * You will be able to get the final text result only after you are done talking.
     *
     * @param  useGoogleIme  true or false whether to use GoogleVoiceIme or not (false by default)
     * @param  prompt       the text prompt to display on the GoogleVoiceIme dialog.
     *                      pass Null to use the default prompt.
     */
    public void useGoogleImeRecognition(boolean useGoogleIme, @Nullable  String prompt){

        if(prompt != null)
            this.googleImePrompt = prompt;

        this.useGoogleIme = useGoogleIme;
    }

    public void startSpeechRecognition(){
         /*
         * Set the SpeechRecognizerListener and SpeechRecognitionPermissionListener here
         * so that a later call to setSpeechRecognitionListener() or setSpeechRecognitionPermissionListener()
         * can still affect SpeechRecognition when you start listening
         */

        checkProperties();
        SpeechRecognitionListener speechRecognitionListener = new SpeechRecognitionListener(
                this.onSpeechRecognitionListener, context);

        if(!speechRecognitionPermissions.isPermissionGiven(context)){

            if(!handlePermissions)
                throw new SecurityException(context.getString(R.string.security_exception_text));

            speechRecognitionPermissions.setSpeechRecognitionPermissionListener(this.onSpeechRecognitionPermissionListener);
            speechRecognitionPermissions.requestPermissions();

        }else{

            /**
             * Trigger the  OnSpeechRecognitionStarted() callback to notify client that
             * SpeechRecognition has started listening.
             * NOTE: do this here and not in {@link SpeechRecognitionListener} so that
             * GoogleIme can still notify via same Listener
             */
            onSpeechRecognitionListener.OnSpeechRecognitionStarted();
            if(useGoogleIme){

                googleImeSpeechRecognition.setVoicePrompt(googleImePrompt);
                googleImeSpeechRecognition.setSpeechRecognitionListener(speechRecognitionListener);
                googleImeSpeechRecognition.startGoogleIme();
                return;
            }

            speechRecognizer.setRecognitionListener(speechRecognitionListener);
            speechRecognizer.startListening(recognizerIntent);
        }
    }

    public void stopSpeechRecognition(){

        onSpeechRecognitionListener.OnSpeechRecognitionStopped();
        speechRecognizer.stopListening();

        if(speechRecognizer != null)
            speechRecognizer.destroy();

        //remove the fragments
        ((Activity)context).getFragmentManager().beginTransaction().remove(speechRecognitionPermissions).commit();
        ((Activity)context).getFragmentManager().beginTransaction().remove(googleImeSpeechRecognition).commit();

    }

    private void initializeGoogleVoiceImeParameters(){

        googleImeSpeechRecognition = new GoogleImeSpeechRecognition();
        ((Activity) context).getFragmentManager()
                .beginTransaction()
                .add(googleImeSpeechRecognition, SpeechRecognition.class.getSimpleName())
                .commit();
    }

    private void initializeSpeechRecognitionParameters(){

        if(!SpeechRecognitionUtilities.isSpeechRecognitionEnabled(context))
            throw new IllegalStateException(context.getString(R.string.speech_not_enabled_exception_text));

         /*
          * Initialize the SpeechRecognitionPermissions and googleIme here
          * for lazy loading the fragments
         */
        initializeGoogleVoiceImeParameters();
        speechRecognitionPermissions = new SpeechRecognitionPermissions();
        ((Activity) context).getFragmentManager()
                .beginTransaction()
                .add(speechRecognitionPermissions, SpeechRecognition.class.getSimpleName())
                .commit();

         /*
         *Initialize the SpeechRecognizer and set listener with onSpeechRecognizerListener implemented by client
         */
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);

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
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
        }

        //TODO: Set preferred Speech recognition Language
    }


    private void checkProperties(){
        if(onSpeechRecognitionListener == null)
            throw new NullPointerException(context.getString(R.string.speech_listener_null_exception_text));

        if(handlePermissions){
            if(onSpeechRecognitionPermissionListener == null)
                throw new NullPointerException(context.getString(R.string.permission_listener_null_exception_text));
        }

        if(speechRecognitionPermissions == null)
            throw new NullPointerException();

        if(speechRecognizer == null)
            throw new NullPointerException();
    }

}
