#import "BottomTabPresenterCreator.h"
#import "RNNTabBarItemCreator.h"
#import "RNNTabBarItemAppearanceCreator.h"
#import "RNNTabBarItemIOS15Creator.h"

@implementation BottomTabPresenterCreator

+ (BottomTabPresenter *)createWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
    RNNTabBarItemCreator *tabCreator;
    if (@available(iOS 15.0, *)) {
        tabCreator = [RNNTabBarItemIOS15Creator new];
    } else if (@available(iOS 13.0, *)) {
        tabCreator = [RNNTabBarItemAppearanceCreator new];
    } else {
        tabCreator = [RNNTabBarItemCreator new];
    }

    return [[BottomTabPresenter alloc] initWithDefaultOptions:defaultOptions tabCreator:tabCreator];
}

@end
