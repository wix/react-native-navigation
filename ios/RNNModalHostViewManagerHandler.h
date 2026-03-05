#import "RNNModalManager.h"
#import <Foundation/Foundation.h>
#ifndef RCT_NEW_ARCH_ENABLED
#import <React/RCTModalHostViewManager.h>
#endif

@interface RNNModalHostViewManagerHandler : NSObject

- (instancetype)initWithModalManager:(RNNModalManager *)modalManager;

#ifndef RCT_NEW_ARCH_ENABLED
- (void)connectModalHostViewManager:(RCTModalHostViewManager *)modalHostViewManager;
#endif

@end
