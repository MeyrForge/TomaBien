# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepattributes *Annotation*

-keepattributes Signature

-keepattributes SourceFile,LineNumberTable

# ################################# REGLAS PARA ROOM ##################################
#
-keep class com.meyrforge.tomabien.medication_tracker.data.entities.** { *; }
-keep class com.meyrforge.tomabien.my_medications.data.entities.** { *; }

# ################################# REGLAS PARA HILT ##################################
#
# Hilt es muy bueno proveyendo sus propias reglas. Normalmente no necesitas añadir nada manual,
# pero si tuvieras problemas, estas son las reglas de seguridad que se suelen usar.
# NO las añadas a menos que encuentres un error relacionado con Hilt.
# -keep class dagger.hilt.internal.aggregatedroot.InitializableRoots
# -keep class *.HiltComponents.*
# -keep class **.*_HiltModules.*
# -keep @dagger.hilt.InstallIn class *
# -dontwarn dagger.hilt.internal.aggregatedroot.AggregatedRoot

# ############################### REGLAS PARA COMPOSE #################################

-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}
-keep class androidx.compose.runtime.Composer

# Si usas @Preview, esta regla evita que se eliminen.
-keepclassmembers class **.R$* {
    public static <fields>;
}

