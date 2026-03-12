#import "RNNModalManager.h"
#import <Foundation/Foundation.h>

#ifndef RCT_REMOVE_LEGACY_ARCH
#import <React/RCTModalHostViewManager.h>
#endif

@interface RNNModalHostViewManagerHandler : NSObject

- (instancetype)initWithModalManager:(RNNModalManager *)modalManager;

#ifndef RCT_REMOVE_LEGACY_ARCH
- (void)connectModalHostViewManager:(RCTModalHostViewManager *)modalHostViewManager;
#endif

@end
