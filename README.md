 ![](https://jitpack.io/v/maxwellobi/Android-Speech-Recognition.svg)
 ![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)
 
## Android Speech Recognition
This library lets you perform continuous voice recognition in your android app with options to either use Google Voice Ime in a Dialog or perform the recognition in-app. It also provides you with an option to enforce ONLY **offline speech recognition**.

## Installation
**Gradle** 

Add the Jitpack repository to your root build.gradle file
```java
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency to your app's build.gradle file
```java
dependencies {
	compile 'com.github.maxwellobi:android-speech-recognition:v1.0.0-beta.1'
}
```
**Maven** 

Add the repository to your pom.xml file
```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```
Add the dependency
```xml 
<dependency>
    <groupId>com.github.maxwellobi</groupId>
    <artifactId>android-speech-recognition</artifactId>
    <version>v1.0.0-beta.1</version>
</dependency>
```
## Usage

To use `SpeechRecognition` library, initialize it, set a listener for handling callbacks, then start listening. 
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	SpeechRecognition speechRecognition = new SpeechRecognition(this);
	speechRecognition.setSpeechRecognitionPermissionListener(this);
	speechRecognition.setSpeechRecognitionListener(this);

	Button speakButton = findViewById(R.id.button);
    speakButton.setOnClickListener(new View.OnClickListener(){
	    @Override
	    public void onClick(View view) {
		    speechRecognition.startSpeechRecognition();   
	    }
	});
}
``` 
### **Handling Permissions**
 By default, `SpeechRecognition` handles the request to grant RECORD_AUDIO permission internally and provides a callback via `OnSpeechRecognitionPermissionListener` interface. There is no need to request this permission in your manifest.

```java
@Override
public void onPermissionGranted() {
	//RECORD_AUDIO permission was granted
}
@Override
public void onPermissionDenied() {
	//RECORD_AUDIO permission was denied
}
```
**Note:** use `SpeechRecognition.handleAudioPermissions(false)` to disable this behavior. If this is set but the permission was not granted, SpeechRecognition will throw a `SecurityException`

**Note:** Runtime permission request is available from API level 23 (Marshmallow). Lower API levels will request permissions on install, thus no callback will be triggered.

### Callbacks/Listeners
To receive the speech recognition result and other event notification, you need to implement the `OnSpeechRecognitionListener`
```java
@Override
public void OnSpeechRecognitionStarted() {}

@Override
public void OnSpeechRecognitionStopped() {}

@Override
public void OnSpeechRecognitionFinalResult(String s) {
	//triggered when SpeechRecognition is done listening.
	//it returns the translated text from the voice input
}
@Override
public void OnSpeechRecognitionCurrentResult(String s) {
	//this is called multiple times when SpeechRecognition is
	//still listening. It returns each recognized word when the user is still speaking
}
@Override
public void OnSpeechRecognitionError(int i, String s) {}
```

**Note** `OnSpeechRecognitionCurrentResult` will NOT be called if your are using the `GoogleVoiceIme` dialog.

### Options

| Method	| Default |	Description   |
|-----------|---------|---------------|
| `setContext(Context)` | Null | Use this to change `SpeechRecognition` current context|
|`setPreferredLanguage(String)`	|-	| Not yet implemented	|
|`handleAudioPermissions(boolean)` | true | set true to handle audio permissions yourself. If `true`, then no need for `setSpeechRecognitionPermissionListener()` |
| `useOnlyOfflineRecognition(boolean)` | false | set true to force android to use its Offline Recognition Engine. By default, android uses either or both online and offline. `false` is recommended. |
| `isSpeechRecognitionAvailable()` | - | returns true or false whether the device supports speech recognition or not |
| `useGoogleImeRecognition(boolean, String)` | false | whether to use GoogleVoiceIme Dialog or not. If true, set a `prompt` to display on the dialog or null to use default. |
| `startSpeechRecognition()` | - | start listening for voice input |
| `stopSpeechRecognition()` | - | dispose resources |

### Errors
See [SpeechRecognizer class](https://developer.android.com/reference/android/speech/SpeechRecognizer.html) for error definitions.


## License

Copyright (c) 2018 Maxwell Obi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
