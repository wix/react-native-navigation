#import "RNNNavigationOptions.h"

@protocol RNNRootViewProtocol <NSObject, UINavigationControllerDelegate>

@required

- (BOOL)isCustomTransitioned;
- (NSString *)componentId;

@end


