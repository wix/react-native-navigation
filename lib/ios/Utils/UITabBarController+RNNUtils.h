#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface UITabBarController (RNNUtils)
- (UIView *)getTabView:(int)tabIndex;

- (UIView *)getTabIcon:(int)tabIndex;
@end