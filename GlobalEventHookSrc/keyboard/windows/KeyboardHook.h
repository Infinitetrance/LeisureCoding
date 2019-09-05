
#include <jni.h>

#ifndef KEYBOARD_HOOK_H
#define KEYBOARD_HOOK_H
#ifdef __cplusplus
extern "C" {
#endif

typedef enum {
    E_SUCCESS = 0,
	E_NO_HANDLE_METHOD = -1,
	E_HOOK_FAILED = -2,
	E_UNHOOK_FAILED = -3,
	E_NOTIFY_FAILED = -4
} GlobalKeyboardHookError;

/*
 * Class:     GlobalKeyboardHook$NativeKeyboardHook
 * Method:    registerHook
 * Signature: ()V
 */
JNIEXPORT jint JNICALL Java_lc_kra_system_keyboard_GlobalKeyboardHook_00024NativeKeyboardHook_registerHook(JNIEnv *,jobject);
/*
 * Class:     GlobalKeyboardHook$NativeKeyboardHook
 * Method:    unregisterHook
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_lc_kra_system_keyboard_GlobalKeyboardHook_00024NativeKeyboardHook_unregisterHook(JNIEnv *,jobject);

#ifdef __cplusplus
}
#endif
#endif
