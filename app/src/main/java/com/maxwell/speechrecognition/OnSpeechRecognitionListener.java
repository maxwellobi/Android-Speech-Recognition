package com.maxwell.speechrecognition;

/**
 * Created by Maxwell on 14-Jan-18.
 */

public interface OnSpeechRecognitionListener {

    /**
     * This callback is fired once you call {@link SpeechRecognition#startSpeechRecognition()}
     * to inform you that speech recognition has started listening.
     */
    void OnSpeechRecognitionStarted();

    /**
     * This callback is the opposite of {@link #OnSpeechRecognitionStarted()}.
     * It is fired once you call {@link SpeechRecognition#stopSpeechRecognition()}
     * and informs you that speech recognition has stopped listening.
     */
    void OnSpeechRecognitionStopped();

    /**
     * Returns the translates text when SpeechRecognition is complete,
     * i.e when it is done translating your speech to text.
     * Note: when this callback is triggered, it means SpeechRecognition
     * is not actively listening but has not been explicitly stopped via
     * {@link SpeechRecognition#stopSpeechRecognition()}. You will need
     * to start speech recognition again to continue listening after this.
     *
     * @param finalSentence translated text to return from speech
     */
    void OnSpeechRecognitionFinalResult(String finalSentence);


    /**
     * Returns the text it is currently translating.
     * This callback is triggered multiple times even when SpeechRecognition
     * is still listening. It returns the live result of its translations
     *
     * @param currentWord current translated word
     */
    void OnSpeechRecognitionCurrentResult(String currentWord);

    /**
     * This callback is fired if there is an error with {@link SpeechRecognition}.
     * It returns an errorCode and errorMsg for describing the type of error
     * that occurred
     *
     * @param errorCode An integer that indicates the error category.
     *                 (Static defined in {@link android.speech.SpeechRecognizer})
     *                  errorCode cannot be 0. A value less than zero (i.e -1) indicates an
     *                  undefined error.
     * @param errorMsg The description of the error
     */
    void OnSpeechRecognitionError(int errorCode, String errorMsg);
}
