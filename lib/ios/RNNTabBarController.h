
#import <UIKit/UIKit.h>
#import "RNNRootViewProtocol.h"

@interface RNNTabBarController : UITabBarController <RNNRootViewProtocol>

- (void)setTabBarHidden:(BOOL)hidden animated:(BOOL)animated;
- (void)setSelectedIndexByContainerID:(NSString *)containerID;

@end
