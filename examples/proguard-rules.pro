-dontwarn sun.reflect.**
-dontwarn sun.misc.**
-dontwarn java.beans.**
-dontwarn sun.nio.ch.**
-dontwarn java.lang.invoke.**

-keep,allowshrinking class com.esotericsoftware.** {
   <fields>;
   <methods>;
}
-keep,allowshrinking class com.esotericsoftware.kryo.** { *; }
-keep,allowshrinking class com.esotericsoftware.kryo.io.** { *; }
