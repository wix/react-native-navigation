#pragma once
#import "RNNCommandsHandler.h"
#ifdef RCT_NEW_ARCH_ENABLED
#import <rnnavigation/rnnavigation.h>
#else
#import <React/RCTBridgeModule.h>
#endif

@interface RNNTurboModule : NSObject
#ifdef RCT_NEW_ARCH_ENABLED
                          <NativeRNNTurboModuleSpec>
#else
                          <RCTBridgeModule>
#endif

@end
