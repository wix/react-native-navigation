#!/bin/bash

# Apply Unique Header Guards - Type 1 Fixes
# This script applies unique header guards to prevent redefinition errors

echo "Applying unique header guards to React Native source files..."

RN_PATH="../node_modules/react-native/ReactCommon"

# Function to apply header guard to a file
apply_header_guard() {
    local file_path="$1"
    local guard_name="$2"
    
    if [ -f "$file_path" ]; then
        # Check if #pragma once exists before trying to replace it
        if grep -q "#pragma once" "$file_path"; then
            echo "  Replacing #pragma once with guard $guard_name in $(basename "$file_path")"
            
            # Replace #pragma once with unique header guard
            sed -i '' "s/#pragma once/#ifndef $guard_name\n#define $guard_name/" "$file_path"
            
            # Add #endif at the end of the file if it doesn't exist
            if ! tail -1 "$file_path" | grep -q "#endif"; then
                echo "    Adding #endif to end of file"
                echo "" >> "$file_path"
                echo "#endif // $guard_name" >> "$file_path"
            else
                echo "    ✅ #endif already exists"
            fi
        else
            # Check if file already has our header guard
            if grep -q "#ifndef $guard_name" "$file_path"; then
                echo "  ✅ $(basename "$file_path") already has correct header guard"
            else
                echo "  ⚠️  $(basename "$file_path") has no #pragma once (may already have different header guards)"
            fi
        fi
    else
        echo "  ❌ File not found: $file_path"
    fi
}

# List of all files that need unique header guards (from manual edit log)
declare -a FILES_AND_GUARDS=(
    # Type 1: Unique Header Guards (23 files from manual edits)
    "jsinspector-modern/InspectorInterfaces.h:RNN_STATIC_FRAMEWORKS_INSPECTORINTERFACES_H"
    "jsinspector-modern/UniqueMonostate.h:RNN_STATIC_FRAMEWORKS_UNIQUEMONOSTATE_H"
    "jsinspector-modern/ExecutionContext.h:RNN_STATIC_FRAMEWORKS_EXECUTIONCONTEXT_H"
    "jsinspector-modern/StackTrace.h:RNN_STATIC_FRAMEWORKS_STACKTRACE_H"
    "jsinspector-modern/ConsoleMessage.h:RNN_STATIC_FRAMEWORKS_CONSOLEMESSAGE_H"
    "jsinspector-modern/CdpJson.h:RNN_STATIC_FRAMEWORKS_CDPJSON_H"
    "jsinspector-modern/RuntimeAgentDelegate.h:RNN_STATIC_FRAMEWORKS_RUNTIMEAGENTDELEGATE_H"
    "jsinspector-modern/RuntimeAgent.h:RNN_STATIC_FRAMEWORKS_RUNTIMEAGENT_H"
    "jsinspector-modern/ScopedExecutor.h:RNN_STATIC_FRAMEWORKS_SCOPEDEXECUTOR_H"
    "jsinspector-modern/WeakList.h:RNN_STATIC_FRAMEWORKS_WEAKLIST_H"
    "jsinspector-modern/RuntimeTarget.h:RNN_STATIC_FRAMEWORKS_RUNTIMETARGET_H"
    "jsinspector-modern/SessionState.h:RNN_STATIC_FRAMEWORKS_SESSIONSTATE_H"
    "jsinspector-modern/FallbackRuntimeTargetDelegate.h:RNN_STATIC_FRAMEWORKS_FALLBACKRUNTIMETARGETDELEGATE_H"
    "jsinspector-modern/ExecutionContextManager.h:RNN_STATIC_FRAMEWORKS_EXECUTIONCONTEXTMANAGER_H"
    "jsinspector-modern/HostCommand.h:RNN_STATIC_FRAMEWORKS_HOSTCOMMAND_H"
    "jsinspector-modern/InstanceTarget.h:RNN_STATIC_FRAMEWORKS_INSTANCETARGET_H"
    "jsinspector-modern/NetworkIOAgent.h:RNN_STATIC_FRAMEWORKS_NETWORKIOAGENT_H"
    "jsinspector-modern/Base64.h:RNN_STATIC_FRAMEWORKS_BASE64_H"
    "jsinspector-modern/FallbackRuntimeAgentDelegate.h:RNN_STATIC_FRAMEWORKS_FALLBACKRUNTIMEAGENTDELEGATE_H"
    "jsinspector-modern/HostAgent.h:RNN_STATIC_FRAMEWORKS_HOSTAGENT_H"
    "jsinspector-modern/HostTarget.h:RNN_STATIC_FRAMEWORKS_HOSTTARGET_H"
    "jsinspector-modern/InspectorFlags.h:RNN_STATIC_FRAMEWORKS_INSPECTORFLAGS_H"
    "jsinspector-modern/InspectorPackagerConnection.h:RNN_STATIC_FRAMEWORKS_INSPECTORPACKAGERCONNECTION_H"
    "jsinspector-modern/InspectorPackagerConnectionImpl.h:RNN_STATIC_FRAMEWORKS_INSPECTORPACKAGERCONNECTIONIMPL_H"
    "jsinspector-modern/InspectorUtilities.h:RNN_STATIC_FRAMEWORKS_INSPECTORUTILITIES_H"
    "jsinspector-modern/InstanceAgent.h:RNN_STATIC_FRAMEWORKS_INSTANCEAGENT_H"
    "jsinspector-modern/ReactCdp.h:RNN_STATIC_FRAMEWORKS_REACTCDP_H"
    "jsinspector-modern/TracingAgent.h:RNN_STATIC_FRAMEWORKS_TRACINGAGENT_H"
    "jsinspector-modern/Utf8.h:RNN_STATIC_FRAMEWORKS_UTF8_H"
    "jsinspector-modern/WebSocketInterfaces.h:RNN_STATIC_FRAMEWORKS_WEBSOCKETINTERFACES_H"
    "react/runtime/JSRuntimeFactory.h:RNN_STATIC_FRAMEWORKS_JSRUNTIMEFACTORY_H"
    "react/runtime/TimerManager.h:RNN_STATIC_FRAMEWORKS_TIMERMANAGER_H"
    "react/runtime/PlatformTimerRegistry.h:RNN_STATIC_FRAMEWORKS_PLATFORMTIMERREGISTRY_H"
    "react/renderer/runtimescheduler/RuntimeScheduler.h:RNN_STATIC_FRAMEWORKS_RUNTIMESCHEDULER_H"
    "react/renderer/runtimescheduler/RuntimeSchedulerEventTimingDelegate.h:RNN_STATIC_FRAMEWORKS_RUNTIMESCHEDULEREVENTTIMINGDELEGATE_H"
    "react/renderer/consistency/ShadowTreeRevisionConsistencyManager.h:RNN_STATIC_FRAMEWORKS_SHADOWTREEREVISIONCONSISTENCYMANAGER_H"
    "react/renderer/runtimescheduler/Task.h:RNN_STATIC_FRAMEWORKS_TASK_H"
    "react/renderer/runtimescheduler/SchedulerPriorityUtils.h:RNN_STATIC_FRAMEWORKS_SCHEDULERPRIORITYUTILS_H"
    "react/performance/timeline/PerformanceEntry.h:RNN_STATIC_FRAMEWORKS_PERFORMANCEENTRY_H"
    "react/performance/timeline/CircularBuffer.h:RNN_STATIC_FRAMEWORKS_CIRCULARBUFFER_H"
    "react/performance/timeline/PerformanceEntryBuffer.h:RNN_STATIC_FRAMEWORKS_PERFORMANCEENTRYBUFFER_H"
    "react/performance/timeline/PerformanceEntryCircularBuffer.h:RNN_STATIC_FRAMEWORKS_PERFORMANCEENTRYCIRCULARBUFFER_H"
    "react/performance/timeline/PerformanceEntryKeyedBuffer.h:RNN_STATIC_FRAMEWORKS_PERFORMANCEENTRYKEYEDBUFFER_H"
    "react/performance/timeline/PerformanceObserverRegistry.h:RNN_STATIC_FRAMEWORKS_PERFORMANCEOBSERVERREGISTRY_H"
    "react/performance/timeline/PerformanceEntryReporter.h:RNN_STATIC_FRAMEWORKS_PERFORMANCEENTRYREPORTER_H"
    "react/renderer/core/ShadowNode.h:RNN_STATIC_FRAMEWORKS_SHADOWNODE_H"
    "react/renderer/components/view/ConcreteViewShadowNode.h:RNN_STATIC_FRAMEWORKS_CONCRETEVIEWSHADOWNODE_H"
    "react/renderer/components/text/ParagraphState.h:RNN_STATIC_FRAMEWORKS_PARAGRAPHSTATE_H"
    "react/renderer/textlayoutmanager/TextMeasureCache.h:RNN_STATIC_FRAMEWORKS_TEXTMEASURECACHE_H"
    "react/renderer/core/Props.h:RNN_STATIC_FRAMEWORKS_PROPS_H"
    "react/renderer/core/PropsParserContext.h:RNN_STATIC_FRAMEWORKS_PROPSPARSERCONTEXT_H"
    "react/renderer/attributedstring/ParagraphAttributes.h:RNN_STATIC_FRAMEWORKS_PARAGRAPHATTRIBUTES_H"
    "react/renderer/attributedstring/AttributedString.h:RNN_STATIC_FRAMEWORKS_ATTRIBUTEDSTRING_H"
    "react/renderer/core/LayoutConstraints.h:RNN_STATIC_FRAMEWORKS_LAYOUTCONSTRAINTS_H"
    "react/renderer/attributedstring/AttributedStringBox.h:RNN_STATIC_FRAMEWORKS_ATTRIBUTEDSTRINGBOX_H"
    "react/utils/ContextContainer.h:RNN_STATIC_FRAMEWORKS_CONTEXTCONTAINER_H"
)

# Process all files
echo "Processing ${#FILES_AND_GUARDS[@]} files for unique header guards..."
for file_and_guard in "${FILES_AND_GUARDS[@]}"; do
    # Split the string by colon
    file_path="${file_and_guard%:*}"
    guard_name="${file_and_guard#*:}"
    
    apply_header_guard "$RN_PATH/$file_path" "$guard_name"
done

echo "✅ Header guard application complete!"
