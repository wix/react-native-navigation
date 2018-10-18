#import <Foundation/Foundation.h>
#import "RNNLayoutProtocol.h"

@interface RNNDefaultOptionsHelper : NSObject

+ (void)recrusivelySetDefaultOptions:(RNNNavigationOptions *)defaultOptions onRootViewController:(UIViewController *)rootViewController;

@end
