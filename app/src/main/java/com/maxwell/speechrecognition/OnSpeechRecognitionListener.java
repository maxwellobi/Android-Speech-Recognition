package com.maxwell.speechrecognition;

/**
 * Created by Maxwell on 14-Jan-18.
 */

public interface OnSpeechRecognitionListener {

    void OnSpeechRecognitionStarted();

    void OnSpeechRecognitionStopped();

    void OnSpeechRecognitionFinalResult(String finalSentence);

    void OnSpeechRecognitionCurrentResult(String currentWord);

    void OnSpeechRecognitionError(int errorCode, String errorMsg);
}
