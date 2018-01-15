package com.maxwell.speechrecognition;

import android.os.Bundle;
import android.speech.RecognitionListener;

/**
 * Created by Maxwell on 14-Jan-18.
 */

final class SpeechRecognitionListener implements RecognitionListener {

    private OnSpeechRecognitionListener onSpeechRecognitionListener;

    public SpeechRecognitionListener(OnSpeechRecognitionListener onSpeechRecognizerListener){
        this.onSpeechRecognitionListener = onSpeechRecognizerListener;
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int i) {

    }

    @Override
    public void onResults(Bundle bundle) {

    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
}
