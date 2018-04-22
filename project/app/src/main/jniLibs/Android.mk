LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := app
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	C:\Users\yas_birouk\AndroidStudioProjects\project\app\src\main\jniLibs\armeabi-v7a\libopencv_java.so \
	C:\Users\yas_birouk\AndroidStudioProjects\project\app\src\main\jniLibs\x86\libopencv_java.so \

LOCAL_C_INCLUDES += C:\Users\yas_birouk\AndroidStudioProjects\project\app\src\main\jni
LOCAL_C_INCLUDES += C:\Users\yas_birouk\AndroidStudioProjects\project\app\src\debug\jni
LOCAL_C_INCLUDES += C:\Users\yas_birouk\AndroidStudioProjects\project\app\src\main\jniLibs

include $(BUILD_SHARED_LIBRARY)
