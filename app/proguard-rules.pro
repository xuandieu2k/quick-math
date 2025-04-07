# Preserve type information for Gson to correctly map data types
-keepattributes Signature
-keepattributes *Annotation*

# Keep Gson classes to avoid errors
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

# Keep type information for classes that use reflection, especially for generic classes
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Giữ lại adapter của Gson nếu có sử dụng custom adapter
-keep class com.google.gson.TypeAdapter { *; }
-keep class com.google.gson.JsonDeserializer { *; }
-keep class com.google.gson.JsonSerializer { *; }
-keep class com.google.gson.Gson { *; }

# Keep Retrofit classes to avoid errors
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
# Keep generic signature of Call, Response (R8 full mode signature strips from non-kept items).
 -keep,allowobfuscation,allowshrinking interface retrofit2.Call
 -keep,allowobfuscation,allowshrinking class retrofit2.Response

 # With R8 full mode generic signatures are stripped for classes that are not
 # kept. Suspend functions are wrapped in continuations where the type argument
 # is used.
 -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# Keep OkHttp classes to ensure Retrofit can work properly
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# Retain your project's layers, including domain, data, and model
-keep class com.dhug.example.data.local.entities.** { *; }
-keep class com.dhug.example.data.local.dto.** { *; }
-keep class com.dhug.example.data.remote.** { *; }

# Keep classes using Hilt annotations (if using Dagger Hilt)
-dontwarn dagger.hilt.**
-keep class dagger.hilt.** { *; }
-keepclassmembers class * {
    @dagger.hilt.android.lifecycle.HiltViewModel <init>(...);
}

# Glide configures to not be deleted when mixing code
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
    <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

# If you use standard Java reflection
-keepnames class * implements java.lang.reflect.Type

# Retain all annotations to ensure compatibility
-keepattributes *Annotation*


# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

# AOP
-adaptclassstrings
-keepattributes InnerClasses, EnclosingMethod, Signature, *Annotation*

-keepnames @org.aspectj.lang.annotation.Aspect class * {
    public <methods>;
}

# Retain necessary classes
-keep class com.cooldev.base.** { *; }
-dontwarn com.cooldev.base.**
-keep class com.google.android.gms.internal.ads.** { *; }
-dontwarn android.media.LoudnessCodecController$OnLoudnessCodecUpdateListener
-dontwarn android.media.LoudnessCodecController

# Giữ lại Google Maps và các class liên quan
-keep class com.google.android.gms.maps.** { *; }
-keep interface com.google.android.gms.maps.** { *; }

# Giữ lại các class trong Google Play Services
-keep class com.google.android.gms.** { *; }

# Giữ lại Dynamite Module
-keep class com.google.android.gms.dynamite.** { *; }

# Giữ lại các class liên quan đến Binder
-keep class * extends android.os.Binder { *; }

-keep class com.dhug.example.utils.SecurityUtils { *; }

# keep worker use to wake up
-keepclassmembers class * extends androidx.work.Worker {
    public <init>(android.content.Context,androidx.work.WorkerParameters);
}

# Giữ lại các provider cần thiết của gRPC
-keep class io.grpc.** { *; }
-keep class io.perfmark.** { *; }
#-keep class META-INF.services.io.grpc.** { *; }
#
## Giữ lại file META-INF/services
#-keepresources META-INF/services/io.grpc.*

-dontwarn io.grpc.internal.DnsNameResolverProvider
-dontwarn io.grpc.internal.PickFirstLoadBalancerProvider
-keep class io.grpc.NameResolverProvider { *; }
-keep class io.grpc.LoadBalancerProvider { *; }

-keep class **.R$drawable { *; }
-keepclassmembers class **.R$drawable { public static <fields>; }
-keep public class * {
    public static final int drawable_*;
}

# Giữ lại toàn bộ file trong calendar
-keep class vn.cooldev.calendar.** { *; }
-keep class com.cooldev.selector.** { *; }

-keep class com.tencent.mmkv.** { *; }