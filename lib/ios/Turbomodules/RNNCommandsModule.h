#pragma once

#ifdef RCT_NEW_ARCH_ENABLED
#import <rnnavigation/rnnavigation.h>
#else
#import <React/RCTBridgeModule.h>
#endif

@interface RNNCommandsModule : NSObject
#ifdef RCT_NEW_ARCH_ENABLED
                          <NativeRNNCommandsModuleSpec>
#else
                          <RCTBridgeModule>
#endif

@end
