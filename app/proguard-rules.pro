# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/ayushkedia/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

## For Support Library
-keep class android.support.v4.** { *; }
-keep class android.support.v7.** { *; }

## For this Project
-keep class com.ayush.wikisearch**{*;}

## Lint ResourceBundle
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

## For Parcelable
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

##-----------------------Begin: progaurd configuration for retrofit----------------------
-dontwarn rx.**

-dontwarn okio.**

-dontwarn com.squareup.okhttp.*

-dontwarn retrofit.appengine.UrlFetchClient

-keepattributes *Annotation*
-keep class com.squareup.** { *; }
-keep class retrofit.** { *; }
-keep interface com.squareup.** { *; }
-keep interface retrofit.** { *;}
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}
##---------------End: proguard configuration for Retrofit  ----------

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

-keepattributes JavascriptInterface
##---------------End: proguard configuration for Gson  ----------

##---------------Begin: proguard configuration for Glide  ----------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
##---------------End: proguard configuration for Glide  ----------