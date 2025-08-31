#!/bin/bash

# Comprehensive Static Framework Fix Script
# This script applies all necessary manual fixes for static framework compatibility

echo "Applying static framework fixes..."

# Determine framework directory - use Xcode environment variables when available
if [ -n "$BUILT_PRODUCTS_DIR" ]; then
    # Running from Xcode build phase - use built-in variables
    FRAMEWORK_DIR="$BUILT_PRODUCTS_DIR/React-RuntimeApple/React_RuntimeApple.framework/Headers"
    # For Xcode builds, also set up the Index framework directory
    DERIVED_DATA_ROOT=$(dirname "$BUILT_PRODUCTS_DIR")
    INDEX_FRAMEWORK_DIR="$DERIVED_DATA_ROOT/Index.noindex/Build/Products/Debug-iphonesimulator/React-RuntimeApple/React_RuntimeApple.framework/Headers"
else
    # Fallback for manual execution - use parent directory name as project name
    PROJECT_NAME=$(basename "$(dirname "$(pwd)")")
    DERIVED_DATA_DIR=$(find ~/Library/Developer/Xcode/DerivedData -name "${PROJECT_NAME}*" -type d | head -1)
    if [ -z "$DERIVED_DATA_DIR" ]; then
        echo "❌ DerivedData directory not found for project: $PROJECT_NAME"
        exit 1
    fi
    FRAMEWORK_DIR="$DERIVED_DATA_DIR/Build/Products/Debug-iphonesimulator/React-RuntimeApple/React_RuntimeApple.framework/Headers"
    INDEX_FRAMEWORK_DIR="$DERIVED_DATA_DIR/Index.noindex/Build/Products/Debug-iphonesimulator/React-RuntimeApple/React_RuntimeApple.framework/Headers"
fi

echo "Framework directory: $FRAMEWORK_DIR"

# Fix 1: Copy JSRuntimeFactory.h to framework if missing
if [ ! -f "$FRAMEWORK_DIR/react/runtime/JSRuntimeFactory.h" ]; then
    echo "Copying JSRuntimeFactory.h to framework..."
    mkdir -p "$FRAMEWORK_DIR/react/runtime"
    cp "$(pwd)/../node_modules/react-native/ReactCommon/react/runtime/JSRuntimeFactory.h" "$FRAMEWORK_DIR/react/runtime/JSRuntimeFactory.h"
fi

# Fix 2: Copy jsinspector-modern headers if missing
if [ ! -d "$FRAMEWORK_DIR/jsinspector-modern" ]; then
    echo "Copying jsinspector-modern headers..."
    mkdir -p "$FRAMEWORK_DIR/jsinspector-modern"
    cp -R "$(pwd)/../node_modules/react-native/ReactCommon/jsinspector-modern/"* "$FRAMEWORK_DIR/jsinspector-modern/"
fi

# Fix 3: Fix JSRuntimeFactory.h include path
echo "Fixing JSRuntimeFactory.h include path..."
if [ -f "$FRAMEWORK_DIR/react/runtime/JSRuntimeFactory.h" ]; then
    sed -i '' 's|#include <jsinspector-modern/ReactCdp.h>|#include "../../jsinspector-modern/ReactCdp.h"|g' "$FRAMEWORK_DIR/react/runtime/JSRuntimeFactory.h"
fi

# Fix 4: Fix RCTInstance.h include path
echo "Fixing RCTInstance.h include path..."
if [ -f "$FRAMEWORK_DIR/ReactCommon/RCTInstance.h" ]; then
    sed -i '' 's|#import <jsinspector-modern/ReactCdp.h>|#import "../jsinspector-modern/ReactCdp.h"|g' "$FRAMEWORK_DIR/ReactCommon/RCTInstance.h"
fi

# Fix 5: Fix jsinspector-modern internal includes
echo "Fixing jsinspector-modern internal includes..."
if [ -d "$FRAMEWORK_DIR/jsinspector-modern" ]; then
    find "$FRAMEWORK_DIR/jsinspector-modern" -name "*.h" -exec sed -i '' 's|#include <jsinspector-modern/\([^>]*\)>|#include "\1"|g' {} \;
fi

# Additional fixes for Index framework directory if it exists
if [ -d "$INDEX_FRAMEWORK_DIR" ]; then
    echo "Applying fixes to Index framework directory..."
    
    # Fix 1: Copy JSRuntimeFactory.h to Index framework if missing
    if [ ! -f "$INDEX_FRAMEWORK_DIR/react/runtime/JSRuntimeFactory.h" ]; then
        mkdir -p "$INDEX_FRAMEWORK_DIR/react/runtime"
        cp "$(pwd)/../node_modules/react-native/ReactCommon/react/runtime/JSRuntimeFactory.h" "$INDEX_FRAMEWORK_DIR/react/runtime/JSRuntimeFactory.h"
    fi
    
    # Fix 2: Copy jsinspector-modern headers to Index framework if missing
    if [ ! -d "$INDEX_FRAMEWORK_DIR/jsinspector-modern" ]; then
        mkdir -p "$INDEX_FRAMEWORK_DIR/jsinspector-modern"
        cp -R "$(pwd)/../node_modules/react-native/ReactCommon/jsinspector-modern/"* "$INDEX_FRAMEWORK_DIR/jsinspector-modern/"
    fi
    
    # Fix 3: Fix JSRuntimeFactory.h include path in Index framework
    if [ -f "$INDEX_FRAMEWORK_DIR/react/runtime/JSRuntimeFactory.h" ]; then
        sed -i '' 's|#include <jsinspector-modern/ReactCdp.h>|#include "../../jsinspector-modern/ReactCdp.h"|g' "$INDEX_FRAMEWORK_DIR/react/runtime/JSRuntimeFactory.h"
    fi
    
    # Fix 4: Fix RCTInstance.h include path in Index framework
    if [ -f "$INDEX_FRAMEWORK_DIR/ReactCommon/RCTInstance.h" ]; then
        sed -i '' 's|#import <jsinspector-modern/ReactCdp.h>|#import "../jsinspector-modern/ReactCdp.h"|g' "$INDEX_FRAMEWORK_DIR/ReactCommon/RCTInstance.h"
    fi
    
    # Fix 5: Fix jsinspector-modern internal includes in Index framework
    if [ -d "$INDEX_FRAMEWORK_DIR/jsinspector-modern" ]; then
        find "$INDEX_FRAMEWORK_DIR/jsinspector-modern" -name "*.h" -exec sed -i '' 's|#include <jsinspector-modern/\([^>]*\)>|#include "\1"|g' {} \;
    fi
fi

echo "✅ Static framework fixes applied successfully!"