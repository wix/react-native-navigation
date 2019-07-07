#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface UITabBar (Utils)
- (UIView *)getTabView:(int)tabIndex;

- (UIView *)getTabIcon:(int)tabIndex;
@end