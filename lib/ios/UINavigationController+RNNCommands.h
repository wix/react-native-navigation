#import <UIKit/UIKit.h>

typedef void (^RNNTransitionCompletionBlock)(void);
typedef void (^RNNPopCompletionBlock)(NSArray *poppedViewControllers);
typedef void (^RNNTransitionRejectionBlock)(NSString *code, NSString *message, NSError *error);

@interface UINavigationController (RNNCommands)

- (void)push:(UIViewController *)newTop
         onTop:(UIViewController *)onTopViewController
      animated:(BOOL)animated
    completion:(RNNTransitionCompletionBlock)completion
     rejection:(RNNTransitionRejectionBlock)rejection;

- (void)popAnimated:(BOOL)animated
         completion:(RNNTransitionCompletionBlock)completion
          rejection:(RNNTransitionRejectionBlock)rejection;

- (void)popTo:(UIViewController *)viewController
      animated:(BOOL)animated
    completion:(RNNPopCompletionBlock)completion
     rejection:(RNNTransitionRejectionBlock)rejection;

- (void)popToRoot:(UIViewController *)viewController
         animated:(BOOL)animated
       completion:(RNNPopCompletionBlock)completion
        rejection:(RNNTransitionRejectionBlock)rejection;

- (void)setStackChildren:(NSArray<UIViewController *> *)children
      fromViewController:(UIViewController *)fromViewController
                animated:(BOOL)animated
              completion:(RNNTransitionCompletionBlock)completion
               rejection:(RNNTransitionRejectionBlock)rejection;

@end
