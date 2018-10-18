#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNBottomTabOptions.h"

@interface RNNTabBarItemCreator : NSObject

+ (UITabBarItem *)updateTabBarItem:(UITabBarItem *)tabItem bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions;

@end
