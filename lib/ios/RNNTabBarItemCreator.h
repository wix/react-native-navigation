#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNBottomTabOptions.h"

@interface RNNTabBarItemCreator : NSObject

+ (UITabBarItem *)createTabBarItem:(RNNBottomTabOptions *)bottomTabOptions;

+ (void)setTitleAttributes:(UITabBarItem *)tabItem titleAttributes:(NSDictionary *)titleAttributes;

+ (void)setSelectedTitleAttributes:(UITabBarItem *)tabItem selectedTitleAttributes:(NSDictionary *)selectedTitleAttributes;

@end
