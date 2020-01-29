
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "ReactNativeNavigation.h"
#import "RNNLayoutInfo.h"

@interface RNNExternalComponentStore : NSObject

- (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;
- (UIViewController *)getExternalComponent:(RNNLayoutInfo *)layoutInfo bridge:(RCTBridge *)bridge;

@end
