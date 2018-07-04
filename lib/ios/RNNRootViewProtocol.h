#import "RNNNavigationOptions.h"
typedef void (^RNNReactViewReadyCompletionBlock)(void);

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
- (void)waitForReactViewRender:(BOOL)wait perform:(RNNReactViewReadyCompletionBlock)readyBlock;

@end


