
#include <jni.h>

#ifndef MOUSE_HOOK_H
#define MOUSE_HOOK_H
#ifdef __cplusplus
extern "C" {
#endif

typedef enum {
    TS_UP = 0,
	TS_DOWN = 1,
	TS_MOVE = 2,
	TS_WHEEL = 3
} GlobalMouseHookTransitionState;

typedef enum {
    E_SUCCESS = 0,
	E_NO_HANDLE_METHOD = -1,
	E_HOOK_FAILED = -2,
	E_UNHOOK_FAILED = -3,
	E_NOTIFY_FAILED = -4
} GlobalMouseHookError;

/*
 * Class:     GlobalMouseHook$NativeMouseHook
 * Method:    registerHook
 * Signature: ()V
 */
JNIEXPORT jint JNICALL Java_lc_kra_system_mouse_GlobalMouseHook_00024NativeMouseHook_registerHook(JNIEnv *,jobject);
/*
 * Class:     GlobalMouseHook$NativeMouseHook
 * Method:    unregisterHook
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_lc_kra_system_mouse_GlobalMouseHook_00024NativeMouseHook_unregisterHook(JNIEnv *,jobject);

#ifdef __cplusplus
}
#endif
#endif
