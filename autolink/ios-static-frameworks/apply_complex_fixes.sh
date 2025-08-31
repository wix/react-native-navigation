#!/bin/bash

# Apply Complex Fixes - Final Type 3 Fixes
# This script applies the most complex changes including:
# - TextLayoutManager.h forward declarations and member variable changes
# - TextLayoutManager.mm constructor/destructor implementation
# - AttributedStringBox.h syntax cleanup

echo "Applying complex fixes to React Native source files..."

RN_PATH="../node_modules/react-native/ReactCommon"

# Function to check if content exists in file
content_exists() {
    local file_path="$1"
    local content="$2"
    
    if [ -f "$file_path" ]; then
        grep -q "$content" "$file_path"
        return $?
    else
        return 1
    fi
}

# Function to add forward declarations to TextLayoutManager.h
add_forward_declarations() {
    local file_path="$RN_PATH/react/renderer/textlayoutmanager/platform/ios/react/renderer/textlayoutmanager/TextLayoutManager.h"
    
    echo "Adding forward declarations to TextLayoutManager.h..."
    
    if [ ! -f "$file_path" ]; then
        echo "  ❌ TextLayoutManager.h not found"
        return 1
    fi
    
    # Check if forward declarations already exist
    if content_exists "$file_path" "struct TextMeasurement;"; then
        echo "  ✅ Forward declarations already exist"
        return 0
    fi
    
    # Add forward declarations after the namespace line
    echo "  Adding forward declarations..."
    sed -i '' '/namespace facebook::react {/a\
\
// Forward declarations\
struct TextMeasurement;\
struct LineMeasurement;\
struct TextLayoutContext;\
using LinesMeasurements = std::vector<LineMeasurement>;\
\
class TextLayoutManager;
' "$file_path"
    
    echo "  ✅ Forward declarations added"
}

# Function to add destructor declaration to TextLayoutManager.h
add_destructor_declaration() {
    local file_path="$RN_PATH/react/renderer/textlayoutmanager/platform/ios/react/renderer/textlayoutmanager/TextLayoutManager.h"
    
    echo "Adding destructor declaration to TextLayoutManager.h..."
    
    if [ ! -f "$file_path" ]; then
        echo "  ❌ TextLayoutManager.h not found"
        return 1
    fi
    
    # Check if destructor already exists
    if content_exists "$file_path" "~TextLayoutManager();"; then
        echo "  ✅ Destructor declaration already exists"
        return 0
    fi
    
    # Add destructor after constructor
    echo "  Adding destructor declaration..."
    sed -i '' '/TextLayoutManager(const ContextContainer::Shared& contextContainer);/a\
  ~TextLayoutManager();
' "$file_path"
    
    echo "  ✅ Destructor declaration added"
}

# Function to change member variables in TextLayoutManager.h
change_member_variables() {
    local file_path="$RN_PATH/react/renderer/textlayoutmanager/platform/ios/react/renderer/textlayoutmanager/TextLayoutManager.h"
    
    echo "Changing member variables in TextLayoutManager.h..."
    
    if [ ! -f "$file_path" ]; then
        echo "  ❌ TextLayoutManager.h not found"
        return 1
    fi
    
    # Check if void* variables already exist
    if content_exists "$file_path" "void\* textMeasureCache_;"; then
        echo "  ✅ Member variables already changed to void*"
        return 0
    fi
    
    # Change member variable types
    echo "  Changing member variable types..."
    sed -i '' 's/std::shared_ptr<TextMeasureCache> textMeasureCache_;/void* textMeasureCache_;/' "$file_path"
    sed -i '' 's/std::shared_ptr<LineMeasureCache> lineMeasureCache_;/void* lineMeasureCache_;/' "$file_path"
    sed -i '' 's/std::unique_ptr<TextMeasureCache> textMeasureCache_;/void* textMeasureCache_;/' "$file_path"
    sed -i '' 's/std::unique_ptr<LineMeasureCache> lineMeasureCache_;/void* lineMeasureCache_;/' "$file_path"
    sed -i '' 's/TextMeasureCache textMeasureCache_{};/void* textMeasureCache_;/' "$file_path"
    sed -i '' 's/LineMeasureCache lineMeasureCache_{};/void* lineMeasureCache_;/' "$file_path"
    
    echo "  ✅ Member variables changed"
}

# Function to fix TextLayoutManager.mm constructor and destructor
fix_textlayoutmanager_mm() {
    local file_path="$RN_PATH/react/renderer/textlayoutmanager/platform/ios/react/renderer/textlayoutmanager/TextLayoutManager.mm"
    
    echo "Fixing TextLayoutManager.mm constructor and destructor..."
    
    if [ ! -f "$file_path" ]; then
        echo "  ❌ TextLayoutManager.mm not found"
        return 1
    fi
    
    # Check if TextMeasureCache include already exists
    if ! content_exists "$file_path" "textlayoutmanager/TextMeasureCache.h"; then
        echo "  Adding TextMeasureCache include..."
        sed -i '' '/^#include "TextLayoutManager.h"/a\
#include "../../../../../../textlayoutmanager/TextMeasureCache.h"
' "$file_path"
        echo "  ✅ Include added"
    else
        echo "  ✅ TextMeasureCache include already exists"
    fi
    
    # Check if constructor already has new TextMeasureCache()
    if content_exists "$file_path" "new TextMeasureCache()"; then
        echo "  ✅ Constructor already fixed"
    else
        echo "  Fixing constructor..."
        # Fix constructor body
        sed -i '' 's/textMeasureCache_ = std::make_shared<TextMeasureCache>();/textMeasureCache_ = new TextMeasureCache();/' "$file_path"
        sed -i '' 's/lineMeasureCache_ = std::make_shared<LineMeasureCache>();/lineMeasureCache_ = new LineMeasureCache();/' "$file_path"
        sed -i '' 's/textMeasureCache_ = std::unique_ptr<TextMeasureCache>(new TextMeasureCache());/textMeasureCache_ = new TextMeasureCache();/' "$file_path"
        sed -i '' 's/lineMeasureCache_ = std::unique_ptr<LineMeasureCache>(new LineMeasureCache());/lineMeasureCache_ = new LineMeasureCache();/' "$file_path"
        echo "  ✅ Constructor fixed"
    fi
    
    # Check if destructor already exists
    if content_exists "$file_path" "~TextLayoutManager()"; then
        echo "  ✅ Destructor already exists"
    else
        echo "  Adding destructor implementation..."
        # Add destructor after constructor - find the constructor closing brace specifically
        sed -i '' '/self_ = wrapManagedObject/,/^}$/{
            /^}$/a\
\
TextLayoutManager::~TextLayoutManager()\
{\
  delete static_cast<TextMeasureCache*>(textMeasureCache_);\
  delete static_cast<LineMeasureCache*>(lineMeasureCache_);\
}
        }' "$file_path"
        echo "  ✅ Destructor added"
    fi
    
    # Fix casting in existing methods
    echo "  Fixing method casting..."
    sed -i '' 's/textMeasureCache_\.get(/static_cast<TextMeasureCache*>(textMeasureCache_)->get(/g' "$file_path"
    sed -i '' 's/lineMeasureCache_\.get(/static_cast<LineMeasureCache*>(lineMeasureCache_)->get(/g' "$file_path"
    echo "  ✅ Method casting fixed"
}

# Function to fix AttributedStringBox.h syntax
fix_attributedstringbox_syntax() {
    local file_path="$RN_PATH/react/renderer/attributedstring/AttributedStringBox.h"
    
    echo "Fixing AttributedStringBox.h syntax..."
    
    if [ ! -f "$file_path" ]; then
        echo "  ❌ AttributedStringBox.h not found"
        return 1
    fi
    
    # Check if extraneous namespace line exists
    if content_exists "$file_path" "} // namespace facebook::react$"; then
        echo "  Removing extraneous namespace line..."
        # Remove the extraneous namespace closing line (keep only the last one)
        sed -i '' '/^} \/\/ namespace facebook::react$/{ N; /\n} \/\/ namespace facebook::react$/d; }' "$file_path"
        echo "  ✅ Extraneous namespace line removed"
    else
        echo "  ✅ AttributedStringBox.h syntax already correct"
    fi
}

echo "Applying complex fixes..."

# Apply all complex fixes
add_forward_declarations
add_destructor_declaration  
change_member_variables
fix_textlayoutmanager_mm
fix_attributedstringbox_syntax

echo "✅ Complex fixes complete!"
