#import <UIKit/UIKit.h>
#import "RNNNavigationControllerPresenter.h"
#import "UINavigationController+RNNOptions.h"
#import "UIViewController+LayoutProtocol.h"

@interface RNNNavigationController : UINavigationController <RNNLayoutProtocol>

@property (nonatomic, retain) RNNNavigationControllerPresenter* presenter;

- (void)setTopBarBackgroundColor:(UIColor *)backgroundColor;

@end
