
#import <UIKit/UIKit.h>

@interface RNNTabBarController : UITabBarController

- (void)setTabBarHidden:(BOOL)hidden animated:(BOOL)animated;
- (void)setSelectedIndexByContainerID:(NSString *)containerID;

@end
