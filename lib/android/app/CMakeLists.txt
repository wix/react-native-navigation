cmake_minimum_required(VERSION 3.4.1)

set (CMAKE_VERBOSE_MAKEFILE ON)
set (CMAKE_CXX_STANDARD 14)
set (CMAKE_CXX_FLAGS "-DFOLLY_NO_CONFIG=1 -DFOLLY_HAVE_CLOCK_GETTIME=1 -DFOLLY_HAVE_MEMRCHR=1 -DFOLLY_USE_LIBCPP=1 -DFOLLY_MOBILE=1 -DON_ANDROID")

set (PACKAGE_NAME "jsi_navigation")
set (BUILD_DIR ${CMAKE_SOURCE_DIR}/build)
### found this build dir
add_library(
        ${PACKAGE_NAME}
        SHARED
        ../../cpp/jsi_navigation.cpp
        ./jsi_jni_adapter.cpp

)

target_include_directories(
        ${PACKAGE_NAME}
        PRIVATE
        "${NODE_MODULES_DIR}/react-native/React"
        "${NODE_MODULES_DIR}/react-native/React/Base"
        "${NODE_MODULES_DIR}/react-native/ReactAndroid/src/main/jni"
        "${NODE_MODULES_DIR}/react-native/ReactAndroid/src/main/java/com/facebook/react/turbomodule/core/jni"
        "${NODE_MODULES_DIR}/react-native/ReactCommon"
        "${NODE_MODULES_DIR}/react-native/ReactCommon/callinvoker"
        "${NODE_MODULES_DIR}/react-native/ReactCommon/jsi"
        "../../cpp"
)

# find libraries

file (GLOB LIBRN_DIR "${BUILD_DIR}/react-native-0*/jni/${ANDROID_ABI}")


find_library(
        LOG_LIB
        log
)

find_library(
        REACT_NATIVE_JNI_LIB
        reactnativejni
        PATHS ${LIBRN_DIR}
        NO_CMAKE_FIND_ROOT_PATH
)

find_library(
        REACT_NATIVE_UTILS_LIB
        reactnativeutilsjni
        PATHS ${LIBRN_DIR}
        NO_CMAKE_FIND_ROOT_PATH
)

target_link_libraries(
        ${PACKAGE_NAME}
        ${LOG_LIB}
        ${REACT_NATIVE_JNI_LIB}
        ${REACT_NATIVE_UTILS_LIB}
        android
)