#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNBottomTabOptions.h"

@interface RNNTabBarItemCreator : NSObject

+ (UITabBarItem *)updateTabBarItem:(UITabBarItem *)tabItem bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions;

+ (void)setTitleAttributes:(UITabBarItem *)tabItem titleAttributes:(NSDictionary *)titleAttributes;

+ (void)setSelectedTitleAttributes:(UITabBarItem *)tabItem selectedTitleAttributes:(NSDictionary *)selectedTitleAttributes;

@end
