
cmake_minimum_required(VERSION 3.4.1)
add_compile_options(-std=c++11)


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


target_link_libraries(
        jsi_demo
        android
        log)
