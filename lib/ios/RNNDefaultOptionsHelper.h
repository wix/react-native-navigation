#import <Foundation/Foundation.h>
#import "RNNLayoutProtocol.h"

@interface RNNDefaultOptionsHelper : NSObject

+ (void)recursivelySetDefaultOptions:(RNNNavigationOptions *)defaultOptions onRootViewController:(UIViewController *)rootViewController;

@end
