# Proguard rules for HMorix Client App
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod

# Retrofit & Gson
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class in.hmorix.client.data.model.** { *; }

# WebKit Javascript Interface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
