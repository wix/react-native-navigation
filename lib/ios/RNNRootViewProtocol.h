#import "RNNNavigationOptions.h"

@protocol RNNRootViewProtocol <NSObject, UINavigationControllerDelegate>

@optional
- (void)setOptions:(RNNNavigationOptions*)options;

@required

- (BOOL)isCustomTransitioned;
- (BOOL)isAnimated;

- (NSString *)componentId;

@end


