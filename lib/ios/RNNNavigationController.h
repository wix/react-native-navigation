#import <UIKit/UIKit.h>
#import "RNNParentProtocol.h"
#import "RNNNavigationControllerPresenter.h"
#import "UINavigationController+RNNOptions.h"

@interface RNNNavigationController : UINavigationController <RNNParentProtocol>

@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, retain) RNNNavigationControllerPresenter* presenter;
@property (nonatomic, strong) RNNNavigationOptions* options;

- (void)setTopBarBackgroundColor:(UIColor *)backgroundColor;

@end
