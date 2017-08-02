#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include <android/log.h>

void trimStr(char *src);
char* xor(char *keys, char *src, int len);
char* generateKey(char *src);
void storeKey(const char *filename, char* key);

// xor生成key的数据池
const char* xorKeyWarhouse = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

/**
  JNI读取安卓asset下的文件需要使用到NDK目录下的asset_manager_jni.h和asset_manager.h提供的接口。

      1．从Java端使用getAssets()得到一个JAVA端的AssetManager对象，通过JNI调用传递到C++端。
      2．C++端使用NDK提供的AAssetManager_fromJava方法得到C++端的AAssetManager的指针用于打开里面的文件。
      3．使用AAssetManager_open方法打开指定文件名的文件得到AAsset*用于获取文件内容和长度，作用和fopen打开文件产生的FILE指针类似。
      4．使用AAsset_getLength获取文件长度，如果文件不是很大可以直接用malloc分配空间使用AAsset_read进度文件内容读取。
      5．读取完毕使用AAsset_close关闭打开的文件。
 **/
JNIEXPORT jstring JNICALL
Java_com_demo_guard_AssetGuard_deGuard(JNIEnv *env, jclass type, jobject assetManager,
                                       jstring assetFileName_) {
    const char *assetFileName = (*env)->GetStringUTFChars(env, assetFileName_, 0);

    AAssetManager* mgr = AAssetManager_fromJava(env, assetManager);

    // 首先读取加密的内容
    AAsset* asset = AAssetManager_open(mgr, assetFileName, AASSET_MODE_STREAMING);
    if (NULL == asset) {
        __android_log_print(ANDROID_LOG_ERROR, "assetGuard", "_ASSET_NOT_FOUND_");
        return JNI_FALSE;
    }

    off_t size = AAsset_getLength(asset);
    char* buffer = (char*) malloc (sizeof(char)*size);
    AAsset_read(asset, buffer, (size_t) size);
    __android_log_print(ANDROID_LOG_INFO, "assetGuard", buffer);
    AAsset_close(asset);

//    // 再去读取对应的key
//    char *keyAssetFileName = NULL;
//    strncpy(keyAssetFileName, assetFileName, strlen(assetFileName)-strlen(".json"));
//    __android_log_print(ANDROID_LOG_ERROR, "assetGuard", keyAssetFileName);
//
//    AAsset* keyAsset = AAssetManager_open(mgr, keyAssetFileName, AASSET_MODE_STREAMING);
//    if (NULL == asset) {
//        __android_log_print(ANDROID_LOG_ERROR, "assetGuard", "_ASSET_KEY_NOT_FOUND_");
//        return JNI_FALSE;
//    }
//
//    off_t keySize = AAsset_getLength(keyAsset);
//    char* keyBuffer = (char*) malloc (sizeof(char)*size);
//    AAsset_read(keyAsset, keyBuffer, (size_t) keySize);
//    __android_log_print(ANDROID_LOG_INFO, "assetGuard", keyBuffer);
//    AAsset_close(keyAsset);
//
//    char* originBuffer = xor(keyBuffer, buffer, strlen(keyBuffer));

    (*env)->ReleaseStringUTFChars(env, assetFileName_, assetFileName);

    return (*env)->NewStringUTF(env, buffer);
}

JNIEXPORT jstring JNICALL
Java_com_demo_guard_AssetGuard_guard(JNIEnv *env, jclass type, jobject assetManager,
                                     jstring assetFileName_) {
    const char *assetFileName = (*env)->GetStringUTFChars(env, assetFileName_, 0);
//    const long BUFSIZ = 8192;

    AAssetManager* mgr = AAssetManager_fromJava(env, assetManager);
    AAssetDir* assetDir = AAssetManager_openDir(mgr, "");
    AAsset* asset = AAssetManager_open(mgr, assetFileName, AASSET_MODE_STREAMING);
    if (NULL == asset) {
        __android_log_print(ANDROID_LOG_ERROR, "assetGuard", "_ASSET_NOT_FOUND_");
        return JNI_FALSE;
    }

    // 可能不支持比较大的文件
    off_t size = AAsset_getLength(asset);
    char* buffer = (char*) malloc (sizeof(char)*size);
    AAsset_read(asset, buffer, (size_t) size);
    AAsset_close(asset);

    // 去掉空格等数据
    // 修剪字符串会产生问题：JNI DETECTED ERROR IN APPLICATION: input is not valid Modified UTF-8: illegal start byte 0xa8
//    trimStr(buffer);
    // 产生keys，并且存储
    char* keys = generateKey(buffer);
    __android_log_print(ANDROID_LOG_INFO, "assetGuard", keys);
    storeKey(assetFileName, keys);
    // xor
    int len = strlen(buffer);
    char* xorBuffer = xor(keys, buffer, len);
    __android_log_print(ANDROID_LOG_INFO, "assetGuard", xorBuffer);

    (*env)->ReleaseStringUTFChars(env, assetFileName_, assetFileName);
    return (*env)->NewStringUTF(env, xorBuffer);
}

// 修剪string，去掉无用的空格、换行符等
void trimStr(char *str)
{
    char * tail = str;
    char * next = str;

    while(*next)
    {
        if(*next != ' ')
        {
            if(tail < next)
                *tail = *next;
            tail++;
        }
        next++;
    }
    *tail = '\0';
}

char* xor(char *keys, char *src, int len)
{
    char* xorStr = (char*) malloc (sizeof(char)*len);
    for (int i = 0; i < len; i++)
    {
        xorStr[i] = src[i] ^ keys[i];
    }
//    __android_log_print(ANDROID_LOG_INFO, "xxx", xorStr);
//
//    char* xorStr2 = (char*) malloc (sizeof(char)*len);
//    for (int i = 0; i < len; i++)
//    {
//        xorStr2[i] = xorStr[i] ^ keys[i];
//    }
//    __android_log_print(ANDROID_LOG_INFO, "xxx", xorStr2);

    return xorStr;
}

unsigned int getRandomIndex(int low, int up)
{
    unsigned int uiResult;

    if (low > up)
    {
        int temp = low;
        low = up;
        up = temp;
    }

    uiResult = (unsigned int) ((rand() % (up - low + 1)) + low);

    return uiResult;
}

char* generateKey(char *src)
{
    int len = strlen(src);
    char* keys = (char*) malloc (sizeof(char)*len);
    for (int i = 0; i < len; ++i)
    {
        unsigned int index = getRandomIndex(0, 55);
        keys[i] = xorKeyWarhouse[index];
    }
    return keys;
}

void storeKey(const char *filename, char* key)
{
    FILE* out;
    if((out=fopen(filename, "w"))==NULL) {
        __android_log_print(ANDROID_LOG_ERROR, "assetGuard", "open store key file error");
        return;
    }
    fputs(key, out);
    fclose(out);
}