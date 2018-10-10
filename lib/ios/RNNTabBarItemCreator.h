#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNTabBarItemCreator : NSObject

+ (UITabBarItem *)updateTabBarItem:(UITabBarItem *)tabItem text:(NSString *)text textColor:(NSDictionary *)textColor selectedTextColor:(NSDictionary *)selectedTextColor icon:(NSDictionary *)icon selectedIcon:(NSDictionary *)selectedIcon iconInsets:(id)iconInsets iconColor:(NSDictionary *)iconColor selectedIconColor:(NSDictionary *)selectedIconColor fontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize;

@end
