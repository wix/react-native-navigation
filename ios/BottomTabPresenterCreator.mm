#import "BottomTabPresenterCreator.h"
#import "RNNTabBarItemCreator.h"
#import "TabBarItemAppearanceCreator.h"
#import "TabBarItemIOS15Creator.h"

@implementation BottomTabPresenterCreator

+ (BottomTabPresenter *)createWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
    RNNTabBarItemCreator *tabCreator;
    if (@available(iOS 15.0, *)) {
        tabCreator = [TabBarItemIOS15Creator new];
    } else if (@available(iOS 13.0, *)) {
        tabCreator = [TabBarItemAppearanceCreator new];
    } else {
        tabCreator = [RNNTabBarItemCreator new];
    }

    return [[BottomTabPresenter alloc] initWithDefaultOptions:defaultOptions tabCreator:tabCreator];
}

@end
