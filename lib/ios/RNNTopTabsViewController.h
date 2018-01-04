#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <React/RCTUIManager.h>
#import "RNNRootViewProtocol.h"

@interface RNNTopTabsViewController : UIViewController <RNNRootViewProtocol>

@property (nonatomic, retain) UIView* contentView;

- (void)setViewControllers:(NSArray*)viewControllers;
- (void)viewController:(UIViewController*)vc changedTitle:(NSString*)title;
- (instancetype)init;

@end
