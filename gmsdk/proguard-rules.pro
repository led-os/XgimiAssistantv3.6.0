# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Program Files (x86)\Android\sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile



# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
#
# Starting with version 2.2 of the Android plugin for Gradle, these files are no longer used. Newer
# versions are distributed with the plugin and unpacked at build time. Files in this directory are
# no longer maintained.

#忽略警告
-ignorewarnings
#保证是独立的jar,没有任何项目引用,如果不写就会认为我们所有的代码是无用的,从而把所有的代码压缩掉,导出一个空的jar
-dontshrink
#保护泛型
-keepattributes Signature

-libraryjars 'C:\Program Files\Java\jdk1.8.0_31\jre\lib\rt.jar'

-libraryjars 'C:\Program Files (x86)\Android\sdk\platforms\android-22\android.jar'

-dontwarn com.google.gson.**
-keep class com.google.gson.**{*;}
-keep class com.xgimi.gmsdk.bean.reply.**{*;}
-keep class com.xgimi.gmsdk.bean.send.**{*;}
-keep class com.xgimi.gmsdk.connect.GMDeviceProxyFactory{*;}
-keep class com.xgimi.gmsdk.connect.IGMDeviceProxy{*;}
-keep class com.xgimi.gmsdk.bean.send.GMKeyCommand{*;}
-keep class com.xgimi.gmsdk.bean.device.GMDevice{*;}
-keep class com.xgimi.gmsdk.callback.GMConnectListeners{*;}
-keep class com.xgimi.gmsdk.callback.OnXGimiTouchListener{*;}
-keep class com.xgimi.gmsdk.callback.IGMDeviceConnectedListener{*;}
-keep class com.xgimi.gmsdk.callback.IGMDeviceFoundListener{*;}
-keep class com.xgimi.gmsdk.callback.IGMReceivedScreenShotResultListener{*;}
-keep class com.xgimi.gmsdk.connect.XGIMILOG{*;}

#-keep class com.xgimi.gmsdk.* {
#
#public <fields>;
#
#public <methods>;
#
#}
