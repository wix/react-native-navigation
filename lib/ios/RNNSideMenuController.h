#import <UIKit/UIKit.h>
#import "RNNSideMenuChildVC.h"
#import "MMDrawerController.h"
#import "RNNParentProtocol.h"

@interface RNNSideMenuController : MMDrawerController <RNNParentProtocol>

@property (readonly) RNNSideMenuChildVC *center;
@property (readonly) RNNSideMenuChildVC *left;
@property (readonly) RNNSideMenuChildVC *right;

@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, retain) RNNViewControllerPresenter* presenter;
@property (nonatomic, strong) RNNNavigationOptions* options;

-(void)showSideMenu:(MMDrawerSide)side animated:(BOOL)animated;
-(void)hideSideMenu:(MMDrawerSide)side animated:(BOOL)animated;

@end
