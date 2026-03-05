
#import "RNNLayoutInfo.h"
#import "ReactNativeNavigation.h"
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNExternalComponentStore : NSObject

#ifdef RCT_NEW_ARCH_ENABLED
- (void)registerExternalHostComponent:(NSString *)name callback:(RNNExternalHostViewCreator)callback;
- (UIViewController *)getExternalHostComponent:(RNNLayoutInfo *)layoutInfo host:(RCTHost *)host;
#else
- (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;
- (UIViewController *)getExternalComponent:(RNNLayoutInfo *)layoutInfo bridge:(RCTBridge *)bridge;
#endif

@end
