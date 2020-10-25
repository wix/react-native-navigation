
#import "RNNLayoutInfo.h"
#import "ReactNativeNavigation.h"
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNExternalComponentStore : NSObject

- (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;
- (UIViewController *)getExternalComponent:(RNNLayoutInfo *)layoutInfo bridge:(RCTBridge *)bridge;

@end
