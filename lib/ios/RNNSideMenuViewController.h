#import "MMDrawerController.h"
#import "RNNSideMenuChildViewController.h"
#import "RNNSideMenuPresenter.h"
#import "UIViewController+LayoutProtocol.h"
#import <UIKit/UIKit.h>

@interface RNNSideMenuViewController : MMDrawerController <RNNLayoutProtocol>

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
                           creator:(id<RNNComponentViewCreator>)creator
              childViewControllers:(NSArray *)childViewControllers
                           options:(RNNNavigationOptions *)options
                    defaultOptions:(RNNNavigationOptions *)defaultOptions
                         presenter:(RNNBasePresenter *)presenter
                      eventEmitter:(RNNEventEmitter *)eventEmitter;

@property(readonly) RNNSideMenuChildViewController *center;
@property(readonly) RNNSideMenuChildViewController *left;
@property(readonly) RNNSideMenuChildViewController *right;

- (void)side:(MMDrawerSide)side enabled:(BOOL)enabled;
- (void)side:(MMDrawerSide)side visible:(BOOL)visible;
- (void)side:(MMDrawerSide)side width:(double)width;
- (void)setAnimationType:(NSString *)animationType;

@end
