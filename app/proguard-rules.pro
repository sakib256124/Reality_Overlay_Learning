# TensorFlow Lite uses native delegates and model metadata classes that should
# remain intact when release minification is enabled later.
-keep class org.tensorflow.lite.** { *; }
-keep class org.tensorflow.lite.support.** { *; }

# Firebase model serialization and Room entities are accessed reflectively.
-keep class com.rola.app.data.database.entities.** { *; }
-keep class com.rola.app.domain.model.** { *; }
-keep class com.google.firebase.** { *; }

# Keep monitoring wrappers and BuildConfig keys useful in obfuscated crash reports.
-keep class com.rola.app.core.monitoring.** { *; }
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ARCore/SceneView use native and reflective rendering hooks.
-keep class com.google.ar.core.** { *; }
-keep class io.github.sceneview.** { *; }

# Android platform TextToSpeech callbacks should remain intact.
-keep class android.speech.tts.** { *; }
