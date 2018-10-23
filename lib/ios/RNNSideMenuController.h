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

- (void)side:(MMDrawerSide)side enabled:(BOOL)enabled;
- (void)side:(MMDrawerSide)side visible:(BOOL)visible;
- (void)side:(MMDrawerSide)side width:(double)width;
- (void)setAnimationType:(NSString *)animationType;

@end
