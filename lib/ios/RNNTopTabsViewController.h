#import <React/RCTUIManager.h>
#import "RNNLayoutProtocol.h"

@interface RNNTopTabsViewController : UIViewController <RNNLayoutProtocol>

@property (nonatomic, retain) UIView* contentView;

- (void)setViewControllers:(NSArray*)viewControllers;
- (void)viewController:(UIViewController*)vc changedTitle:(NSString*)title;
- (instancetype)init;

@end
