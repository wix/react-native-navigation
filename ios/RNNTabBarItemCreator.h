#import "RNNBottomTabOptions.h"
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNTabBarItemCreator : NSObject

- (UITabBarItem *)createTabBarItem:(RNNBottomTabOptions *)bottomTabOptions
                         mergeItem:(UITabBarItem *)mergeItem;

- (UITabBarItem *)createTabBarItem:(UITabBarItem *)mergeItem;

- (void)setTitleAttributes:(UITabBarItem *)tabItem titleAttributes:(NSDictionary *)titleAttributes;

- (void)setSelectedTitleAttributes:(UITabBarItem *)tabItem
           selectedTitleAttributes:(NSDictionary *)selectedTitleAttributes;

@end
