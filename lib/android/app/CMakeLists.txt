
cmake_minimum_required(VERSION 3.4.1)
execute_process (COMMAND ln "-s" "src" "../../../node_modules/react-native/third-party/double-conversion-1.1.6/double-conversion")

include_directories(
        ../../../node_modules/react-native/React
        ../../../node_modules/react-native/React/Base
        ../../../node_modules/react-native/ReactCommon
        ../../../node_modules/react-native/ReactCommon/jsi
        src/main/cpp/demo/include/
)

add_definitions(
        -DFOLLY_USE_LIBCPP=1
        -DFOLLY_NO_CONFIG=1
        -DFOLLY_HAVE_MEMRCHR=1
)
add_library( # Specifies the name of the library.
             jsi_demo

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             ../../../node_modules/react-native/ReactCommon/jsi/jsi/jsi.cpp
             src/main/cpp/demo/jsi_demo.cpp )

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

target_link_libraries( # Specifies the target library.
        jsi_demo
        android
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})
