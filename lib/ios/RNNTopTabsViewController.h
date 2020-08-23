#import <React/RCTUIManager.h>
#import "RNNLayoutProtocol.h"
#import "RNNUIBarButtonItem.h"

@interface RNNTopTabsViewController : UIViewController <RNNLayoutProtocol, UIScrollViewDelegate>

@property (nonatomic, retain) UIScrollView* contentView;

- (void)setViewControllers:(NSArray*)viewControllers;
- (void)viewController:(UIViewController*)vc changedTitle:(NSString*)title;
- (instancetype)init;
- (void)onButtonPress:(RNNUIBarButtonItem *)barButtonItem;

@end
