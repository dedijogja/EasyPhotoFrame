#include <jni.h>
#include <string>

//key text sudah diganti, key asset sudah diganti ke EFP Studios
std::string keyDesText = "LY5ext&kEy;5Ak~U";
std::string keyDesAssets = "hrWQzTkXKsL/LRxAS4qtcg==";

//kode iklan sudah diubah ke EFP Studios
std::string adInterstitial =        "NfeG}PEPUnE((q*q)*_5*#2@$%%!%@^+#7=7&(";
std::string adBanner =              "NfeG}PEPUnE((q*q)*_5*#2@$%%!#)+$)740$$";

//startApp sudah diganti EFP Studios
std::string startAppId = "%q-(#$Q@!";

//pesan eror sudah diganti EFP Studios
std::string smesek = "h] UUH}NG~YHE.1yR|>k6";

//area package, mempersulit proses replace String .so file dengan memisahkan menjadi beberapa bagian
std::string awalP = "com.ef";
std::string tengahP = "pstudios.";
std::string ahirP = "woodphotoframe";
std::string finalPackage = awalP + tengahP + ahirP;

jstring dapatkanPackage(JNIEnv* env, jobject activity) {
    jclass android_content_Context =env->GetObjectClass(activity);
    jmethodID midGetPackageName = env->GetMethodID(android_content_Context,"getPackageName", "()Ljava/lang/String;");
    jstring packageName= (jstring)env->CallObjectMethod(activity, midGetPackageName);
    return packageName;
}

const char * cekStatus(JNIEnv* env, jobject activity, const char * text){
    if(strcmp(env->GetStringUTFChars(dapatkanPackage(env, activity), NULL), finalPackage.c_str()) != 0){
        jclass Exception = env->FindClass("java/lang/RuntimeException");
        env->ThrowNew(Exception, "JNI Failed!");
        return NULL;
    }
    return text;
}


extern "C"
JNIEXPORT jstring JNICALL Java_com_efpstudios_constant_NativeComunicate_keyDesText(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, keyDesText.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_efpstudios_constant_NativeComunicate_keyDesAssets(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, keyDesAssets.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_efpstudios_constant_NativeComunicate_packageName(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, finalPackage.c_str()));
}


extern "C"
JNIEXPORT jstring JNICALL Java_com_efpstudios_constant_NativeComunicate_adInterstitial(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, adInterstitial.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_efpstudios_constant_NativeComunicate_adBanner(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, adBanner.c_str()));
}


extern "C"
JNIEXPORT jstring JNICALL Java_com_efpstudios_constant_NativeComunicate_startAppId(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, startAppId.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_efpstudios_constant_NativeComunicate_smesek(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, smesek.c_str()));
}
