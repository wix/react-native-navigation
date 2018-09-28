#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <React/RCTUIManager.h>
#import "RNNParentProtocol.h"

@interface RNNTopTabsViewController : UIViewController <RNNParentProtocol>

@property (nonatomic, retain) UIView* contentView;
@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, retain) RNNBasePresenter* presenter;

- (void)setViewControllers:(NSArray*)viewControllers;
- (void)viewController:(UIViewController*)vc changedTitle:(NSString*)title;
- (instancetype)init;

@end
