#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

typedef void (^RNNTransitionCompletionBlock)(void);
typedef void (^RNNPopCompletionBlock)(NSArray* poppedViewControllers);
typedef void (^RNNTransitionRejectionBlock)(NSString *code, NSString *message, NSError *error);

@interface RNNNavigationStackManager : NSObject

- (void)push:(UIViewController *)newTop onTop:(UIViewController *)onTopViewController animated:(BOOL)animated animationDelegate:(id)animationDelegate completion:(RNNTransitionCompletionBlock)completion rejection:(RNNTransitionRejectionBlock)rejection;

- (void)pop:(UIViewController *)viewController animated:(BOOL)animated completion:(RNNTransitionCompletionBlock)completion rejection:(RNNTransitionRejectionBlock)rejection;

- (void)popTo:(UIViewController *)viewController animated:(BOOL)animated completion:(RNNPopCompletionBlock)completion rejection:(RNNTransitionRejectionBlock)rejection;

- (void)popToRoot:(UIViewController*)viewController animated:(BOOL)animated completion:(RNNPopCompletionBlock)completion rejection:(RNNTransitionRejectionBlock)rejection;

- (void)setStackRoot:(UIViewController *)newRoot fromViewController:(UIViewController *)fromViewController animated:(BOOL)animated completion:(RNNTransitionCompletionBlock)completion rejection:(RNNTransitionRejectionBlock)rejection;

@end
