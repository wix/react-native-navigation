#import <Foundation/Foundation.h>
#import "RNNNavigationOptions.h"

@interface RNNBasePresenter : NSObject

- (void)present:(RNNNavigationOptions *)options onViewControllerDidLoad:(UIViewController *)viewController;
- (void)present:(RNNNavigationOptions *)options onViewControllerWillAppear:(UIViewController *)viewController;

@end
