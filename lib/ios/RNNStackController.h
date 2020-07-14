#import <UIKit/UIKit.h>
#import "RNNStackPresenter.h"
#import "UINavigationController+RNNOptions.h"
#import "UINavigationController+RNNCommands.h"
#import "UIViewController+LayoutProtocol.h"

@interface RNNStackController : UINavigationController <RNNLayoutProtocol>

@property (nonatomic, retain) RNNStackPresenter* presenter;

@end
