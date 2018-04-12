#import "RNNNavigationOptions.h"

@protocol RNNRootViewProtocol <NSObject, UINavigationControllerDelegate, UIViewControllerTransitioningDelegate>

@optional
- (void)mergeOptions:(NSDictionary*)options;
- (BOOL)isCustomViewController;
- (void)performOnRotation:(void (^)(void))block;
- (void)optionsUpdated;

@required
- (BOOL)isCustomTransitioned;
- (RNNNavigationOptions*)options;
- (NSString *)componentId;

@end


