#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNNavigationOptions.h"

@interface RNNViewControllerPresenter : NSObject

@property (nonatomic, weak) id bindedViewController;

- (void)bindViewController:(UIViewController *)bindedViewController;

- (void)presentOnWillMoveToParent:(RNNNavigationOptions *)options;

- (void)presentOnViewWillAppear:(RNNNavigationOptions *)options;

@end
