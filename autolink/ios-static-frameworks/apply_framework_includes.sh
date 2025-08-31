#!/bin/bash

# Apply Framework Include Path Corrections - Type 2 Fixes
# This script fixes #include and #import statements to use correct framework paths

echo "Applying framework include path corrections to React Native source files..."

RN_PATH="../node_modules/react-native/ReactCommon"
RNN_PATH="../node_modules/react-native-navigation/lib/ios"

# Check if we can find the React Native files
if [ ! -d "$RN_PATH" ]; then
    echo "  Warning: React Native path not found at $RN_PATH"
    echo "  Checking current directory structure..."
    
    # Try to find React Native from current directory
    if [ -d "node_modules/react-native/ReactCommon" ]; then
        RN_PATH="node_modules/react-native/ReactCommon"
        echo "  Found React Native at: $RN_PATH"
    elif [ -d "../node_modules/react-native/ReactCommon" ]; then
        RN_PATH="../node_modules/react-native/ReactCommon"
        echo "  Found React Native at: $RN_PATH"
    else
        echo "  ❌ Could not find React Native ReactCommon directory"
        echo "  Skipping React Native core fixes"
        exit 0
    fi
fi



# Function to apply framework include fix to a file
apply_framework_include() {
    local file_path="$1"
    local old_include="$2"
    local new_include="$3"
    local description="$4"
    
    if [ -f "$file_path" ]; then
        # Check if the old include exists in the file
        if grep -q "$old_include" "$file_path"; then
            echo "  $description in $(basename "$file_path")"
            echo "    $old_include -> $new_include"
            
            # Escape special characters for sed
            old_escaped=$(echo "$old_include" | sed 's/[[\.*^$()+?{|]/\\&/g')
            new_escaped=$(echo "$new_include" | sed 's/[[\.*^$()+?{|]/\\&/g')
            
            # Apply the replacement
            sed -i '' "s|$old_escaped|$new_escaped|g" "$file_path"
            echo "    Applied"
        else
            # Check if the new include already exists
            if grep -q "$new_include" "$file_path"; then
                echo "  ✅ $(basename "$file_path") already has correct include: $new_include"
            else
                echo "  ⚠️  $(basename "$file_path") - old include not found: $old_include"
            fi
        fi
    else
        echo "  ❌ File not found: $file_path"
    fi
}

# List of all framework include fixes (from manual edit log)
declare -a FRAMEWORK_FIXES=(
    # RCTInstance.h fixes (Manual Edit 13, 14, 15)
    "$RN_PATH/react/runtime/platform/ios/ReactCommon/RCTInstance.h:#import <jsinspector-modern/ReactCdp.h>:#import <jsinspector_modern/ReactCdp.h>:Fix jsinspector-modern import"
    "$RN_PATH/react/runtime/platform/ios/ReactCommon/RCTInstance.h:#import <react/runtime/JSRuntimeFactory.h>:#import <React_RuntimeCore/react/runtime/JSRuntimeFactory.h>:Fix JSRuntimeFactory framework path"
    "$RN_PATH/react/runtime/platform/ios/ReactCommon/RCTInstance.h:#import <react/runtime/ReactInstance.h>:#import <React_RuntimeCore/react/runtime/ReactInstance.h>:Fix ReactInstance framework path"
    
    # ReactInstance.h fixes (Manual Edit 16, 18)
    "$RN_PATH/react/runtime/ReactInstance.h:#include <jserrorhandler/JsErrorHandler.h>:#include <React_jserrorhandler/jserrorhandler/JsErrorHandler.h>:Fix jserrorhandler framework path"
    "$RN_PATH/react/runtime/ReactInstance.h:#include <jsinspector-modern/ReactCdp.h>:#include <jsinspector_modern/ReactCdp.h>:Fix jsinspector-modern import"
    "$RN_PATH/react/runtime/ReactInstance.h:#include <react/renderer/runtimescheduler/RuntimeScheduler.h>:#include <React_runtimescheduler/react/renderer/runtimescheduler/RuntimeScheduler.h>:Fix RuntimeScheduler framework path"
    "$RN_PATH/react/runtime/ReactInstance.h:#include <react/runtime/BufferedRuntimeExecutor.h>:#include <React_RuntimeCore/react/runtime/BufferedRuntimeExecutor.h>:Fix BufferedRuntimeExecutor framework path"
    "$RN_PATH/react/runtime/ReactInstance.h:#include <react/runtime/JSRuntimeFactory.h>:#include <React_RuntimeCore/react/runtime/JSRuntimeFactory.h>:Fix JSRuntimeFactory framework path"
    "$RN_PATH/react/runtime/ReactInstance.h:#include <react/runtime/TimerManager.h>:#include <React_RuntimeCore/react/runtime/TimerManager.h>:Fix TimerManager framework path"
    
    # JSExecutor.h fixes (Manual Edit 17)
    "$RN_PATH/cxxreact/JSExecutor.h:#include <jsinspector-modern/InspectorInterfaces.h>:#include <jsinspector_modern/InspectorInterfaces.h>:Fix InspectorInterfaces import"
    "$RN_PATH/cxxreact/JSExecutor.h:#include <jsinspector-modern/ReactCdp.h>:#include <jsinspector_modern/ReactCdp.h>:Fix ReactCdp import"
    
    # RuntimeScheduler.h fixes (Manual Edit 21)
    "$RN_PATH/react/renderer/runtimescheduler/RuntimeScheduler.h:#include <react/performance/timeline/PerformanceEntryReporter.h>:#include <React_performancetimeline/react/performance/timeline/PerformanceEntryReporter.h>:Fix PerformanceEntryReporter framework path"
    "$RN_PATH/react/renderer/runtimescheduler/RuntimeScheduler.h:#include <react/renderer/consistency/ShadowTreeRevisionConsistencyManager.h>:#include <React_rendererconsistency/react/renderer/consistency/ShadowTreeRevisionConsistencyManager.h>:Fix ShadowTreeRevisionConsistencyManager framework path"
    "$RN_PATH/react/renderer/runtimescheduler/RuntimeScheduler.h:#include <react/renderer/runtimescheduler/RuntimeSchedulerClock.h>:#include <React_runtimescheduler/react/renderer/runtimescheduler/RuntimeSchedulerClock.h>:Fix RuntimeSchedulerClock framework path"
    "$RN_PATH/react/renderer/runtimescheduler/RuntimeScheduler.h:#include <react/renderer/runtimescheduler/SchedulerPriorityUtils.h>:#include <React_runtimescheduler/react/renderer/runtimescheduler/SchedulerPriorityUtils.h>:Fix SchedulerPriorityUtils framework path"
    "$RN_PATH/react/renderer/runtimescheduler/RuntimeScheduler.h:#include <react/renderer/runtimescheduler/Task.h>:#include <React_runtimescheduler/react/renderer/runtimescheduler/Task.h>:Fix Task framework path"
    
    # Task.h fixes (after Entry 28)
    "$RN_PATH/react/renderer/runtimescheduler/Task.h:#include <react/renderer/runtimescheduler/RuntimeSchedulerClock.h>:#include <React_runtimescheduler/react/renderer/runtimescheduler/RuntimeSchedulerClock.h>:Fix RuntimeSchedulerClock framework path"
    

    
    # ParagraphComponentDescriptor.h fixes (Manual Edit 23)
    "$RN_PATH/react/renderer/components/text/ParagraphComponentDescriptor.h:#include <react/renderer/components/text/ParagraphShadowNode.h>:#include <React_FabricComponents/react/renderer/components/text/ParagraphShadowNode.h>:Fix ParagraphShadowNode framework path"
    "$RN_PATH/react/renderer/components/text/ParagraphComponentDescriptor.h:#include <react/renderer/core/ConcreteComponentDescriptor.h>:#include <React_Fabric/react/renderer/core/ConcreteComponentDescriptor.h>:Fix ConcreteComponentDescriptor framework path"
    "$RN_PATH/react/renderer/components/text/ParagraphComponentDescriptor.h:#include <react/utils/ContextContainer.h>:#include <React_utils/react/utils/ContextContainer.h>:Fix ContextContainer framework path"
    
    # ParagraphEventEmitter.h fixes (Manual Edit 26, 27)
    "$RN_PATH/react/renderer/components/text/ParagraphEventEmitter.h:#include <react/renderer/components/view/ViewEventEmitter.h>:#include <React_Fabric/react/renderer/components/view/ViewEventEmitter.h>:Fix ViewEventEmitter framework path"
    
    # ParagraphShadowNode.h fixes (after Entry 27)
    "$RN_PATH/react/renderer/components/text/ParagraphShadowNode.h:#include <react/renderer/components/text/BaseTextShadowNode.h>:#include <React_FabricComponents/react/renderer/components/text/BaseTextShadowNode.h>:Fix BaseTextShadowNode framework path"
    "$RN_PATH/react/renderer/components/text/ParagraphShadowNode.h:#include <react/renderer/components/text/ParagraphEventEmitter.h>:#include <React_FabricComponents/react/renderer/components/text/ParagraphEventEmitter.h>:Fix ParagraphEventEmitter framework path"
    "$RN_PATH/react/renderer/components/text/ParagraphShadowNode.h:#include <react/renderer/components/text/ParagraphProps.h>:#include <React_FabricComponents/react/renderer/components/text/ParagraphProps.h>:Fix ParagraphProps framework path"
    "$RN_PATH/react/renderer/components/text/ParagraphShadowNode.h:#include <react/renderer/components/text/ParagraphState.h>:#include <React_FabricComponents/react/renderer/components/text/ParagraphState.h>:Fix ParagraphState framework path"
    "$RN_PATH/react/renderer/components/text/ParagraphShadowNode.h:#include <react/renderer/components/view/ConcreteViewShadowNode.h>:#include <React_Fabric/react/renderer/components/view/ConcreteViewShadowNode.h>:Fix ConcreteViewShadowNode framework path"
    "$RN_PATH/react/renderer/components/text/ParagraphShadowNode.h:#include <react/renderer/core/LayoutContext.h>:#include <React_Fabric/react/renderer/core/LayoutContext.h>:Fix LayoutContext framework path"
    "$RN_PATH/react/renderer/components/text/ParagraphShadowNode.h:#include <react/renderer/core/ShadowNode.h>:#include <React_Fabric/react/renderer/core/ShadowNode.h>:Fix ShadowNode framework path"
    
    # ParagraphProps.h fixes (Manual Edit 29)
    "$RN_PATH/react/renderer/components/text/ParagraphProps.h:#include <react/renderer/attributedstring/ParagraphAttributes.h>:#include <React_Fabric/react/renderer/attributedstring/ParagraphAttributes.h>:Fix ParagraphAttributes framework path"
    "$RN_PATH/react/renderer/components/text/ParagraphProps.h:#include <react/renderer/components/text/BaseTextProps.h>:#include <React_FabricComponents/react/renderer/components/text/BaseTextProps.h>:Fix BaseTextProps framework path"
    "$RN_PATH/react/renderer/components/text/ParagraphProps.h:#include <react/renderer/components/view/ViewProps.h>:#include <React_Fabric/react/renderer/components/view/ViewProps.h>:Fix ViewProps framework path"
    "$RN_PATH/react/renderer/components/text/ParagraphProps.h:#include <react/renderer/core/Props.h>:#include <React_Fabric/react/renderer/core/Props.h>:Fix Props framework path"
    "$RN_PATH/react/renderer/components/text/ParagraphProps.h:#include <react/renderer/core/PropsParserContext.h>:#include <React_Fabric/react/renderer/core/PropsParserContext.h>:Fix PropsParserContext framework path"
    
    # ParagraphState.h fixes (Manual Edit 31)
    "$RN_PATH/react/renderer/components/text/ParagraphState.h:#include <react/debug/react_native_assert.h>:#include <React_debug/react/debug/react_native_assert.h>:Fix react_native_assert framework path"
    "$RN_PATH/react/renderer/components/text/ParagraphState.h:#include <react/renderer/attributedstring/AttributedString.h>:#include <React_Fabric/react/renderer/attributedstring/AttributedString.h>:Fix AttributedString framework path"
    "$RN_PATH/react/renderer/components/text/ParagraphState.h:#include <react/renderer/attributedstring/ParagraphAttributes.h>:#include <React_Fabric/react/renderer/attributedstring/ParagraphAttributes.h>:Fix ParagraphAttributes framework path"
    "$RN_PATH/react/renderer/components/text/ParagraphState.h:#include <react/renderer/mapbuffer/MapBuffer.h>:#include <React_Mapbuffer/react/renderer/mapbuffer/MapBuffer.h>:Fix MapBuffer framework path"

)

# Process all framework include fixes
echo "Processing ${#FRAMEWORK_FIXES[@]} framework include fixes..."
for fix in "${FRAMEWORK_FIXES[@]}"; do
    # Split the string by colons
    IFS=':' read -r file_path old_include new_include description <<< "$fix"
    

    apply_framework_include "$file_path" "$old_include" "$new_include" "$description"
done

echo "✅ Framework include path corrections complete!"
