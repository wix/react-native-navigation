#import "RNNStackPresenter.h"
#import "UINavigationController+RNNCommands.h"
#import "UINavigationController+RNNOptions.h"
#import "UIViewController+LayoutProtocol.h"
#import <UIKit/UIKit.h>

@interface RNNStackController : UINavigationController <RNNLayoutProtocol>

@property(nonatomic, retain) RNNStackPresenter *presenter;

@end
