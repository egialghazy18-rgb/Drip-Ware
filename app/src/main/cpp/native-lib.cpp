#include <jni.h>
#include <string>
#include <android/log.h>
#define LOG_TAG "CheatNative"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
extern "C" JNIEXPORT jstring JNICALL
Java_com_panel_cheat_FloatingService_getNativeStatus(JNIEnv* env, jobject) {
    std::string status = "Native C++ Loaded. Ready to read memory!";
    return env->NewStringUTF(status.c_str());
}
extern "C" JNIEXPORT void JNICALL
Java_com_panel_cheat_FloatingService_activateAimbot(JNIEnv* env, jobject, jboolean isActive) {
    if (isActive) { LOGI("Aimbot AKTIF! (Menunggu Offset Memori)"); } 
    else { LOGI("Aimbot MATI!"); }
}
