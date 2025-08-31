#!/bin/bash

# Apply Special Fixes - Type 3 Fixes
# This script applies non-framework fixes including:
# - Paragraph component special fixes (comments, relative paths)
# - TextLayoutManager include fixes
# - TextInputShadowNode include additions

echo "Applying special fixes to React Native source files..."

RN_PATH="../node_modules/react-native/ReactCommon"

# Function to apply special fix to a file
apply_special_fix() {
    local file_path="$1"
    local old_content="$2"
    local new_content="$3"
    local description="$4"
    
    if [ -f "$file_path" ]; then
        # Check if the old content exists in the file
        if grep -q "$old_content" "$file_path"; then
            echo "  $description in $(basename "$file_path")"
            echo "    Applying fix..."
            
            # Escape special characters for sed
            old_escaped=$(echo "$old_content" | sed 's/[[\.*^$()+?{|]/\\&/g')
            new_escaped=$(echo "$new_content" | sed 's/[[\.*^$()+?{|]/\\&/g')
            
            # Apply the replacement
            sed -i '' "s|$old_escaped|$new_escaped|g" "$file_path"
            echo "    Applied"
        else
            # Check if the new content already exists
            if grep -q "$new_content" "$file_path"; then
                echo "  ✅ $(basename "$file_path") already has correct fix"
            else
                echo "  ⚠️  $(basename "$file_path") - old content not found"
            fi
        fi
    else
        echo "  ❌ File not found: $file_path"
    fi
}

# Function to comment out a line
comment_out_line() {
    local file_path="$1"
    local line_pattern="$2"
    local description="$3"
    
    if [ -f "$file_path" ]; then
        # Check if the line exists and is not already commented
        if grep -q "^#include.*$line_pattern" "$file_path" && ! grep -q "^// #include.*$line_pattern" "$file_path"; then
            echo "  $description in $(basename "$file_path")"
            echo "    Commenting out line..."
            
            # Comment out the line
            sed -i '' "s|^#include.*$line_pattern.*|// & // Temporarily commented for static framework compatibility|g" "$file_path"
            echo "    ✅ Applied"
        else
            # Check if already commented
            if grep -q "^// #include.*$line_pattern" "$file_path"; then
                echo "  ✅ $(basename "$file_path") line already commented out"
            else
                echo "  ⚠️  $(basename "$file_path") - line not found: $line_pattern"
            fi
        fi
    else
        echo "  ❌ File not found: $file_path"
    fi
}

echo "Applying special fixes..."

# Fix 1: Comment out TextLayoutManager includes
echo "Commenting out TextLayoutManager includes..."
comment_out_line "$RN_PATH/react/renderer/components/text/ParagraphComponentDescriptor.h" "textlayoutmanager/TextLayoutManager.h" "Comment out TextLayoutManager include"
comment_out_line "$RN_PATH/react/renderer/components/text/ParagraphShadowNode.h" "textlayoutmanager/TextLayoutManager.h" "Comment out TextLayoutManager include"

# Fix 2: ParagraphEventEmitter.h relative path fix
echo "Applying ParagraphEventEmitter.h relative path fix..."
apply_special_fix "$RN_PATH/react/renderer/components/text/ParagraphEventEmitter.h" \
    "#include <react/renderer/textlayoutmanager/TextMeasureCache.h>" \
    "#include \"../../textlayoutmanager/TextMeasureCache.h\"" \
    "Fix TextMeasureCache relative path"

# Fix 3: ParagraphState.h special TextLayoutManager path
echo "Applying ParagraphState.h special TextLayoutManager path..."
apply_special_fix "$RN_PATH/react/renderer/components/text/ParagraphState.h" \
    "#include <react/renderer/textlayoutmanager/TextLayoutManager.h>" \
    "#include \"../../textlayoutmanager/platform/ios/react/renderer/textlayoutmanager/TextLayoutManager.h\"" \
    "Fix TextLayoutManager iOS-specific path"

# Fix 4: Comment out TextLayoutManager.h includes
echo "Commenting out TextLayoutManager.h includes..."
comment_out_line "$RN_PATH/react/renderer/textlayoutmanager/platform/ios/react/renderer/textlayoutmanager/TextLayoutManager.h" "TextLayoutContext.h" "Comment out TextLayoutContext include"
comment_out_line "$RN_PATH/react/renderer/textlayoutmanager/platform/ios/react/renderer/textlayoutmanager/TextLayoutManager.h" "TextMeasureCache.h" "Comment out TextMeasureCache include"

# Fix 5: TextInputShadowNode.cpp include addition (check if already exists)
echo "Checking TextInputShadowNode.cpp include..."
if [ -f "$RN_PATH/react/renderer/components/textinput/platform/ios/react/renderer/components/iostextinput/TextInputShadowNode.cpp" ]; then
    if grep -q "textlayoutmanager/TextMeasureCache.h" "$RN_PATH/react/renderer/components/textinput/platform/ios/react/renderer/components/iostextinput/TextInputShadowNode.cpp"; then
        echo "  ✅ TextInputShadowNode.cpp already has TextMeasureCache include"
    else
        echo "  📝 Adding TextMeasureCache include to TextInputShadowNode.cpp"
        # Add the include after TextLayoutContext.h
        sed -i '' '/TextLayoutContext\.h/a\
#include "../../../../../../../../textlayoutmanager/TextMeasureCache.h"
' "$RN_PATH/react/renderer/components/textinput/platform/ios/react/renderer/components/iostextinput/TextInputShadowNode.cpp"
        echo "    ✅ Applied"
    fi
else
    echo "  ❌ TextInputShadowNode.cpp not found"
fi

echo "✅ Special fixes complete!"
