#import "RNNNavigationOptions.h"

@protocol RNNRootViewProtocol <NSObject, UINavigationControllerDelegate>

@optional
- (void)mergeOptions:(NSDictionary*)options;

@required

- (BOOL)isCustomTransitioned;
- (BOOL)isAnimated;

- (NSString *)componentId;

@end


