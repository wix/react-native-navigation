#import "RNNNavigationOptions.h"

@protocol RNNRootViewProtocol <NSObject, UINavigationControllerDelegate>

@required

- (BOOL)isCustomTransitioned;
- (BOOL)isAnimated;

@end


