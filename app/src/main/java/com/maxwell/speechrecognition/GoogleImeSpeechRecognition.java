package com.maxwell.speechrecognition;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Maxwell on 15-Jan-18.
 */

public class GoogleImeSpeechRecognition extends Fragment {

    private SpeechRecognitionListener speechRecognitionListener;
    private final int REQUEST_CODE = 123;
    private Intent recognizerIntent;
    private String voicePrompt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        initialize();
    }

    void initialize(){
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, SpeechRecognition.MAX_RESULT_COUNT);
    }

    void setSpeechRecognitionListener(@NonNull SpeechRecognitionListener speechRecognitionListener){
        this.speechRecognitionListener = speechRecognitionListener;
    }

    void setVoicePrompt(String voicePrompt){
        this.voicePrompt = voicePrompt;
    }

    void startGoogleIme(){
        if(recognizerIntent == null) initialize();
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, voicePrompt);

        startActivityForResult(recognizerIntent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){

            /**
             * The matched text with the highest confidence score will be in position 0
             */
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if(matches != null && matches.size() >0){
                String sentence = matches.get(0);
                speechRecognitionListener.getOnSpeechRecognitionListener()
                        .OnSpeechRecognitionFinalResult(sentence);

                return;
            }
        }

        speechRecognitionListener.onError(SpeechRecognizer.ERROR_NO_MATCH);
    }
}
