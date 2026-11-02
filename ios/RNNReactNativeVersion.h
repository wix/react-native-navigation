#pragma once

// Compile-time React Native version detection for RN versions that can't be told apart by
// `__has_include` alone (see RNNAppDelegate.h for the 0.78 / 0.79 checks).
//
// `cxxreact/ReactNativeVersion.h` is C++, so this header must only be imported from .mm files
// and never from a public header.

#if defined(__cplusplus) && __has_include(<cxxreact/ReactNativeVersion.h>)
#include <cxxreact/ReactNativeVersion.h>
#endif

// RN 0.88 added the UIScene life cycle: `RCTIsSceneDelegateApp()` and the
// `+[RCTLinkingManager scene:...]` entry points. In scene apps its app-delegate Linking entry
// points return NO and deliver nothing.
#if defined(REACT_NATIVE_VERSION_MINOR) &&                                                         \
    (REACT_NATIVE_VERSION_MAJOR > 0 || REACT_NATIVE_VERSION_MINOR >= 88)
#define RNN_RN_VERSION_88_OR_NEWER 1
#else
#define RNN_RN_VERSION_88_OR_NEWER 0
#endif
