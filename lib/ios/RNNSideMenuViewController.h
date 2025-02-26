#import "MMDrawerController.h"
#import "RNNSideMenuViewController.h"
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

@property(readonly) RNNSideMenuViewController *center;
@property(readonly) RNNSideMenuViewController *left;
@property(readonly) RNNSideMenuViewController *right;

- (void)side:(MMDrawerSide)side enabled:(BOOL)enabled;
- (void)side:(MMDrawerSide)side visible:(BOOL)visible;
- (void)side:(MMDrawerSide)side width:(double)width;
- (void)setAnimationType:(NSString *)animationType;

@end
