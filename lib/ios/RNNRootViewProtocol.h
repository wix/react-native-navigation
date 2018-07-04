#import "RNNNavigationOptions.h"

@protocol RNNRootViewProtocol <NSObject, UINavigationControllerDelegate, UIViewControllerTransitioningDelegate, UISplitViewControllerDelegate>

@optional
- (void)mergeOptions:(RNNOptions*)options;
- (BOOL)isCustomViewController;
- (void)performOnRotation:(void (^)(void))block;
- (void)optionsUpdated;
- (void)applyModalOptions;
- (BOOL)isCustomTransitioned;
- (RNNNavigationOptions*)options;

@required
- (NSString *)componentId;

@end


