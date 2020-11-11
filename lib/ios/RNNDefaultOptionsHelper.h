#import "RNNLayoutProtocol.h"
#import <Foundation/Foundation.h>

@interface RNNDefaultOptionsHelper : NSObject

+ (void)recursivelySetDefaultOptions:(RNNNavigationOptions *)defaultOptions
                onRootViewController:(UIViewController *)rootViewController;

@end
