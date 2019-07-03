-optimizationpasses 10
-mergeinterfacesaggressively
-overloadaggressively
-dontusemixedcaseclassnames

# Disable Android logging
-assumenosideeffects class android.util.Log { *; }