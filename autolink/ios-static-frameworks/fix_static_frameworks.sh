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

echo "Project name: $PROJECT_NAME"

echo "Framework directory: $FRAMEWORK_DIR"

# Fix 1: Copy JSRuntimeFactory.h to framework if missing
if [ ! -f "$FRAMEWORK_DIR/react/runtime/JSRuntimeFactory.h" ]; then
    echo "Copying JSRuntimeFactory.h to framework..."
    mkdir -p "$FRAMEWORK_DIR/react/runtime"
    cp "$(pwd)/../node_modules/react-native/ReactCommon/react/runtime/JSRuntimeFactory.h" "$FRAMEWORK_DIR/react/runtime/JSRuntimeFactory.h"
fi

# Fix 1b: Also fix JSRuntimeFactory.h includes in React-RuntimeCore framework
# Check all DerivedData directories for RuntimeCore framework (reuse PROJECT_NAME from above)
for DD_DIR in $(find ~/Library/Developer/Xcode/DerivedData -name "${PROJECT_NAME}*" -type d 2>/dev/null); do
    RUNTIME_CORE_DIR="$DD_DIR/Build/Products/Debug-iphonesimulator/React-RuntimeCore/React_RuntimeCore.framework/Headers"
    if [ -f "$RUNTIME_CORE_DIR/react/runtime/JSRuntimeFactory.h" ]; then
        echo "Fixing JSRuntimeFactory.h in RuntimeCore framework ($DD_DIR)..."
        # Copy jsinspector-modern headers to RuntimeCore if missing
        if [ ! -d "$RUNTIME_CORE_DIR/jsinspector-modern" ]; then
            echo "  Copying jsinspector-modern headers to RuntimeCore..."
            cp -R "$(pwd)/../node_modules/react-native/ReactCommon/jsinspector-modern" "$RUNTIME_CORE_DIR/"
        fi
        # Fix the include to use relative path  
        sed -i '' 's|#include <jsinspector-modern/ReactCdp.h>|#include "../../jsinspector-modern/ReactCdp.h"|g' "$RUNTIME_CORE_DIR/react/runtime/JSRuntimeFactory.h"
        
        # Fix ALL jsinspector-modern includes AND imports across the entire DerivedData directory
        echo "  Fixing ALL jsinspector-modern includes/imports in DerivedData..."
        find "$DD_DIR/Build/Products/Debug-iphonesimulator" -name "*.h" -exec grep -l -E "#(include|import) <jsinspector-modern/" {} \; 2>/dev/null | while read file; do
            echo "    Fixing: $file"
            sed -i '' 's|#include <jsinspector-modern/\([^>]*\)>|#include "\1"|g' "$file"
            sed -i '' 's|#import <jsinspector-modern/\([^>]*\)>|#import "\1"|g' "$file"
        done
    fi
done

# Fix 2: Copy jsinspector-modern headers if missing
if [ ! -d "$FRAMEWORK_DIR/jsinspector-modern" ]; then
    echo "Copying jsinspector-modern headers to RuntimeApple framework..."
    mkdir -p "$FRAMEWORK_DIR/jsinspector-modern"
    cp -R "$(pwd)/../node_modules/react-native/ReactCommon/jsinspector-modern/"* "$FRAMEWORK_DIR/jsinspector-modern/"
fi



# Fix 3: Fix JSRuntimeFactory.h include path in RuntimeApple framework
echo "Fixing JSRuntimeFactory.h include path in RuntimeApple framework..."
if [ -f "$FRAMEWORK_DIR/react/runtime/JSRuntimeFactory.h" ]; then
    sed -i '' 's|#include <jsinspector-modern/ReactCdp.h>|#include "../../jsinspector-modern/ReactCdp.h"|g' "$FRAMEWORK_DIR/react/runtime/JSRuntimeFactory.h"
fi



# Fix 3c: Also fix JSRuntimeFactory.h in the source file
PODS_JSRUNTIME="$(pwd)/../node_modules/react-native/ReactCommon/react/runtime/JSRuntimeFactory.h"
if [ -f "$PODS_JSRUNTIME" ]; then
    echo "Fixing source JSRuntimeFactory.h in node_modules..."
    sed -i '' 's|#include <jsinspector-modern/ReactCdp.h>|#include "../../jsinspector-modern/ReactCdp.h"|g' "$PODS_JSRUNTIME"
fi

# Fix 3d: Fix ALL jsinspector-modern includes AND imports in source files
echo "Fixing ALL jsinspector-modern includes/imports in source files..."
find "$(pwd)/../node_modules/react-native/ReactCommon" -name "*.h" -exec grep -l -E "#(include|import) <jsinspector-modern/" {} \; 2>/dev/null | while read file; do
    echo "  Fixing source: $file"
    sed -i '' 's|#include <jsinspector-modern/\([^>]*\)>|#include "\1"|g' "$file"
    sed -i '' 's|#import <jsinspector-modern/\([^>]*\)>|#import "\1"|g' "$file"
done

# Fix 4: Fix RCTInstance.h include path
echo "Fixing RCTInstance.h include path..."
if [ -f "$FRAMEWORK_DIR/ReactCommon/RCTInstance.h" ]; then
    sed -i '' 's|#import <jsinspector-modern/ReactCdp.h>|#import "../jsinspector-modern/ReactCdp.h"|g' "$FRAMEWORK_DIR/ReactCommon/RCTInstance.h"
fi

# Fix 4b: Fix RCTHost.h import path (fix source file in Pods)
echo "Fixing RCTHost.h import path in source..."
# Fix the source file in Pods directory
PODS_RCTHOST="$(pwd)/../node_modules/react-native/ReactCommon/react/runtime/platform/ios/ReactCommon/RCTHost.h"
if [ -f "$PODS_RCTHOST" ]; then
    echo "  Fixing source RCTHost.h in node_modules..."
    sed -i '' 's|#import "../react/runtime/JSRuntimeFactory.h"|#import <React_RuntimeCore/react/runtime/JSRuntimeFactory.h>|g' "$PODS_RCTHOST"
    sed -i '' 's|#import <react/runtime/JSRuntimeFactory.h>|#import <React_RuntimeCore/react/runtime/JSRuntimeFactory.h>|g' "$PODS_RCTHOST"
fi

# Also fix any already-copied files (fallback)
if [ -f "$FRAMEWORK_DIR/ReactCommon/RCTHost.h" ]; then
    echo "  Fixing copied RCTHost.h in DerivedData..."
    sed -i '' 's|#import "../react/runtime/JSRuntimeFactory.h"|#import <React_RuntimeCore/react/runtime/JSRuntimeFactory.h>|g' "$FRAMEWORK_DIR/ReactCommon/RCTHost.h"
    sed -i '' 's|#import <react/runtime/JSRuntimeFactory.h>|#import <React_RuntimeCore/react/runtime/JSRuntimeFactory.h>|g' "$FRAMEWORK_DIR/ReactCommon/RCTHost.h"
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
        echo "  Fixing JSRuntimeFactory.h in Index framework..."
        sed -i '' 's|#include <jsinspector-modern/ReactCdp.h>|#include "../../jsinspector-modern/ReactCdp.h"|g' "$INDEX_FRAMEWORK_DIR/react/runtime/JSRuntimeFactory.h"
    fi
    
    # Fix 3b: Also fix JSRuntimeFactory.h in Index RuntimeCore framework
    INDEX_RUNTIME_CORE_DIR=$(dirname "$INDEX_FRAMEWORK_DIR" | sed 's/React-RuntimeApple/React-RuntimeCore/')/React_RuntimeCore.framework/Headers
    if [ -f "$INDEX_RUNTIME_CORE_DIR/react/runtime/JSRuntimeFactory.h" ]; then
        echo "  Fixing JSRuntimeFactory.h in Index RuntimeCore framework..."
        sed -i '' 's|#include <jsinspector-modern/ReactCdp.h>|#include "../../jsinspector-modern/ReactCdp.h"|g' "$INDEX_RUNTIME_CORE_DIR/react/runtime/JSRuntimeFactory.h"
        
        # Fix ALL jsinspector-modern includes AND imports in Index framework
        INDEX_BASE_DIR=$(dirname "$INDEX_FRAMEWORK_DIR" | sed 's|React-RuntimeApple.*||')
        echo "    Fixing ALL jsinspector-modern includes/imports in Index framework..."
        find "$INDEX_BASE_DIR" -name "*.h" -exec grep -l -E "#(include|import) <jsinspector-modern/" {} \; 2>/dev/null | while read file; do
            echo "      Fixing Index: $file"
            sed -i '' 's|#include <jsinspector-modern/\([^>]*\)>|#include "\1"|g' "$file"
            sed -i '' 's|#import <jsinspector-modern/\([^>]*\)>|#import "\1"|g' "$file"
        done
    fi
    
    # Fix 4: Fix RCTInstance.h include path in Index framework
    if [ -f "$INDEX_FRAMEWORK_DIR/ReactCommon/RCTInstance.h" ]; then
        sed -i '' 's|#import <jsinspector-modern/ReactCdp.h>|#import "../jsinspector-modern/ReactCdp.h"|g' "$INDEX_FRAMEWORK_DIR/ReactCommon/RCTInstance.h"
    fi
    
    # Fix 4b: Fix RCTHost.h import path in Index framework
    if [ -f "$INDEX_FRAMEWORK_DIR/ReactCommon/RCTHost.h" ]; then
        echo "  Fixing RCTHost.h in Index framework..."
        sed -i '' 's|#import "../react/runtime/JSRuntimeFactory.h"|#import <React_RuntimeCore/react/runtime/JSRuntimeFactory.h>|g' "$INDEX_FRAMEWORK_DIR/ReactCommon/RCTHost.h"
        sed -i '' 's|#import <react/runtime/JSRuntimeFactory.h>|#import <React_RuntimeCore/react/runtime/JSRuntimeFactory.h>|g' "$INDEX_FRAMEWORK_DIR/ReactCommon/RCTHost.h"
    fi
    
    # Fix 5: Fix jsinspector-modern internal includes in Index framework
    if [ -d "$INDEX_FRAMEWORK_DIR/jsinspector-modern" ]; then
        find "$INDEX_FRAMEWORK_DIR/jsinspector-modern" -name "*.h" -exec sed -i '' 's|#include <jsinspector-modern/\([^>]*\)>|#include "\1"|g' {} \;
    fi
fi

echo "✅ Static framework fixes applied successfully!"