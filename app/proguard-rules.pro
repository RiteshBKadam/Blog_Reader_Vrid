########################
# Jetpack Compose Rules
########################

# Keep all Jetpack Compose-related classes
-keep class androidx.compose.** { *; }

# Keep all @Composable functions
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# Keep ViewModel-related classes
-keep class androidx.lifecycle.ViewModel { *; }
-keepclassmembers class * extends androidx.lifecycle.ViewModel { *; }

########################
# JSON Parsing (if using Gson or Moshi)
########################

# Keep Gson/Moshi models
-keepclassmembers class com.example.blogreadervrid.model.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.google.gson.** { *; }
-keep class kotlinx.serialization.** { *; }

########################
# Navigation Components
########################

# Keep Jetpack Navigation components (if using Navigation Compose)
-keep class androidx.navigation.** { *; }

########################
# Paging (if using Paging Compose)
########################

# Keep Paging-related classes
-keep class androidx.paging.** { *; }

########################
# Dependency Injection (if using Hilt)
########################

# Keep Dagger-Hilt components
-keep class dagger.hilt.** { *; }
-keep class hilt_aggregated_deps.** { *; }

########################
# Kotlin Coroutines
########################

# Keep coroutine-related classes
-keep class kotlinx.coroutines.** { *; }

########################
# Miscellaneous Android Essentials
########################

# Keep Parcelable classes
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep Activity and Fragment classes
-keep class * extends android.app.Activity { *; }

# Keep Service classes
-keep class * extends android.app.Service { *; }

# Keep BroadcastReceiver classes
-keep class * extends android.content.BroadcastReceiver { *; }

# Keep ContentProviders
-keep class * extends android.content.ContentProvider { *; }

########################
# Logging (Optional)
########################


# Remove Log statements (for production)
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

########################
# R8 Optimization Rules
########################

# Do not remove methods annotated with @Keep
-keep @androidx.annotation.Keep class * { *; }
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

# Optimize code
-dontwarn android.arch.**
-dontwarn org.intellij.lang.annotations.**
-dontwarn kotlin.**
-dontwarn kotlinx.**

# Allow shrinking, obfuscation, and optimization
# Remove these lines if debugging issues
# -dontshrink
# -dontobfuscate
# -dontoptimize
