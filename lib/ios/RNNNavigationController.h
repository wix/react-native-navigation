#import <UIKit/UIKit.h>
#import "RNNParentProtocol.h"
#import "RNNViewControllerPresenter.h"
#import "UINavigationController+RNNOptions.h"

@interface RNNNavigationController : UINavigationController <RNNParentProtocol>

@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, retain) RNNViewControllerPresenter* presenter;
@property (nonatomic, strong) RNNNavigationOptions* options;

- (void)setTopBarBackgroundColor:(UIColor *)backgroundColor;

@end
