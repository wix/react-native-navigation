#import "RNNModalManager.h"
#import <Foundation/Foundation.h>
#import <React/RCTModalHostViewManager.h>

@interface RNNModalHostViewManagerHandler : NSObject

- (instancetype)initWithModalManager:(RNNModalManager *)modalManager;

- (void)connectModalHostViewManager:(RCTModalHostViewManager *)modalHostViewManager;

@end
