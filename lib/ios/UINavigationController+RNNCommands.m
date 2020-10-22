#import "RNNErrorHandler.h"
#import "UINavigationController+RNNCommands.h"
#import <React/RCTI18nUtil.h>

typedef void (^RNNAnimationBlock)(void);

@implementation UINavigationController (RNNCommands)

- (void)push:(UIViewController *)newTop
         onTop:(UIViewController *)onTopViewController
      animated:(BOOL)animated
    completion:(RNNTransitionCompletionBlock)completion
     rejection:(RCTPromiseRejectBlock)rejection {
    if ([[RCTI18nUtil sharedInstance] isRTL]) {
        self.view.semanticContentAttribute = UISemanticContentAttributeForceRightToLeft;
        self.navigationBar.semanticContentAttribute = UISemanticContentAttributeForceRightToLeft;
    } else {
        self.view.semanticContentAttribute = UISemanticContentAttributeForceLeftToRight;
        self.navigationBar.semanticContentAttribute = UISemanticContentAttributeForceLeftToRight;
    }

    [self
        performBlock:^{
          [self pushViewController:newTop animated:animated];
        }
            animated:animated
          completion:completion];
}

- (void)pop:(UIViewController *)viewController
      animated:(BOOL)animated
    completion:(RNNTransitionCompletionBlock)completion
     rejection:(RNNTransitionRejectionBlock)rejection {
    if ([self.viewControllers indexOfObject:viewController] < 0) {
        [RNNErrorHandler reject:rejection
                  withErrorCode:1012
               errorDescription:@"popping component failed"];
        return;
    }

    if ([self topViewController] != viewController) {
        NSMutableArray *vcs = self.viewControllers.mutableCopy;
        [vcs removeObject:viewController];
        [self
            performBlock:^{
              [self setViewControllers:vcs animated:animated];
            }
            animated:animated
            completion:^{
              completion();
            }];
    } else {
        [self
            performBlock:^{
              [self popViewControllerAnimated:animated];
            }
            animated:animated
            completion:^{
              completion();
            }];
    }
}

- (void)popTo:(UIViewController *)viewController
      animated:(BOOL)animated
    completion:(RNNPopCompletionBlock)completion
     rejection:(RNNTransitionRejectionBlock)rejection;
{
    __block NSArray *poppedVCs;
    if ([self.childViewControllers containsObject:viewController]) {
        [self
            performBlock:^{
              poppedVCs = [self popToViewController:viewController animated:animated];
            }
            animated:animated
            completion:^{
              if (completion) {
                  completion(poppedVCs);
              }
            }];
    } else {
        [RNNErrorHandler reject:rejection
                  withErrorCode:1011
               errorDescription:@"component not found in stack"];
    }
}

- (void)popToRoot:(UIViewController *)viewController
         animated:(BOOL)animated
       completion:(RNNPopCompletionBlock)completion
        rejection:(RNNTransitionRejectionBlock)rejection {
    __block NSArray *poppedVCs;
    [self
        performBlock:^{
          poppedVCs = [self popToRootViewControllerAnimated:animated];
        }
        animated:animated
        completion:^{
          completion(poppedVCs);
        }];
}

- (void)setStackChildren:(NSArray<UIViewController *> *)children
      fromViewController:(UIViewController *)fromViewController
                animated:(BOOL)animated
              completion:(RNNTransitionCompletionBlock)completion
               rejection:(RNNTransitionRejectionBlock)rejection {
    [self
        performBlock:^{
          [self setViewControllers:children animated:animated];
        }
            animated:animated
          completion:completion];
}

#pragma mark Private

- (void)performBlock:(RNNAnimationBlock)animationBlock
            animated:(BOOL)animated
          completion:(RNNTransitionCompletionBlock)completion {
    if (!animated) {
        animationBlock();
        if (completion) {
            completion();
        }
        return;
    }

    [CATransaction begin];
    [CATransaction setCompletionBlock:^{
      if (completion) {
          completion();
      }
    }];

    animationBlock();

    [CATransaction commit];
}

@end
