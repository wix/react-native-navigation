#import <UIKit/UIKit.h>
#import "RNNParentProtocol.h"
#import "RNNNavigationControllerPresenter.h"

@interface RNNNavigationController : UINavigationController <RNNParentProtocol>

@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, retain) RNNNavigationControllerPresenter* presenter;

@end
