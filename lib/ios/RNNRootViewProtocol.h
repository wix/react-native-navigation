#import "RNNNavigationOptions.h"

@protocol RNNRootViewProtocol <NSObject, UINavigationControllerDelegate, UIViewControllerTransitioningDelegate>

@optional
- (void)mergeOptions:(NSDictionary*)options;
- (BOOL)isCustomViewController;
- (void)optionsUpdated;
- (void)performOnRotation:(void (^)(void))block;

@required
- (BOOL)isCustomTransitioned;
- (BOOL)isAnimated;
- (RNNNavigationOptions*)options;
- (NSString *)componentId;

@end


