#import "RNNNavigationOptions.h"

@protocol RNNRootViewProtocol <NSObject, UINavigationControllerDelegate>

@optional
- (void)mergeOptions:(NSDictionary*)options;
- (BOOL)isCustomViewController;

@required
- (BOOL)isCustomTransitioned;
- (BOOL)isAnimated;

- (NSString *)componentId;

@end


