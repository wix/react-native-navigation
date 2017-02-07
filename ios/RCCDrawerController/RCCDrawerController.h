#import <UIKit/UIKit.h>
#import "MMDrawerController.h"
#import "RCCDrawerProtocol.h"

#if __has_include("RCTBridge.h")
#import "RCTBridge.h"
#elif __has_include(<React/RCTBridge.h>)
#import <React/RCTBridge.h>
#elif __has_include("React/RCTBridge.h")
#import "React/RCTBridge.h"   // Required when used as a Pod in a Swift project
#endif


@interface RCCDrawerController : MMDrawerController <RCCDrawerDelegate>


@end
