# StrutFit Button Android SDK
SDK for StrutFit Android integration


1. Add it in your root build.gradle at the end of repositories:
```ruby
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
  
2. Add the dependency: replace x.x.x with the desired version
```ruby
  dependencies {
		implementation 'com.github.StrutFit:AndroidSDK:x.x.x'
	}
```

3. Ensure the following permission are given to the application if it does not already:  
	**Internet:** We are accessing a website  
	**Write External Storage:** To allow the webapp to store variable locally  
	**Read External Stoarge:** Allows the app to access camer role for uploading photos  
	**Camera & Audio:** For the in app camera view to allow users to take photos.  
			Although we dont actually record audio we still need the permission.
```ruby
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
```

4.
