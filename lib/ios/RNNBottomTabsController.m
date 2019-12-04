#import "RNNBottomTabsController.h"
#import "RCTConvert+RNNOptions.h"
#import "UITabBarController+RNNUtils.h"

@implementation RNNBottomTabsController {
	NSUInteger _currentTabIndex;
}

- (id<UITabBarControllerDelegate>)delegate {
	return self;
}

- (void)render {
    BottomTabsAttachMode tabsAttachMode = [RCTConvert BottomTabsAttachMode:[[self.resolveOptions withDefault:self.defaultOptions].bottomTabs.tabsAttachMode getWithDefaultValue:@"together"]];
    switch (tabsAttachMode) {
        case BottomTabsAttachModeTogether: {
            [super render];
        }
            break;
        case BottomTabsAttachModeAfterInitialTab: {
            [self.selectedViewController setReactViewReadyCallback:^{
                for (UIViewController* viewController in self.deselectedViewControllers) {
                    [viewController render];
                }
            }];
            [self.selectedViewController render];
        }
            break;
        case BottomTabsAttachModeOnSwitchToTab: {
            [self readyForPresentation];
        }
        break;
        default:
            break;
    }
}

- (void)viewDidLayoutSubviews {
	[self.presenter viewDidLayoutSubviews];
}

- (UIViewController *)getCurrentChild {
	return self.selectedViewController;
}

//- (NSArray<UIViewController *> *)getOtherChildren {
//    self.childViewControllers filteredArrayUsingPredicate:[NSPredicate predicateWithBlock:^BOOL(id  _Nullable evaluatedObject, NSDictionary<NSString *,id> * _Nullable bindings) {
//        return evaluatedObject
//    }]
//}

- (CGFloat)getTopBarHeight {
    for(UIViewController * child in [self childViewControllers]) {
        CGFloat childTopBarHeight = [child getTopBarHeight];
        if (childTopBarHeight > 0) return childTopBarHeight;
    }
    return [super getTopBarHeight];
}

- (void)setSelectedIndexByComponentID:(NSString *)componentID {
	for (id child in self.childViewControllers) {
		UIViewController<RNNLayoutProtocol>* vc = child;

		if ([vc conformsToProtocol:@protocol(RNNLayoutProtocol)] && [vc.layoutInfo.componentId isEqualToString:componentID]) {
			[self setSelectedIndex:[self.childViewControllers indexOfObject:child]];
		}
	}
}

- (void)setSelectedIndex:(NSUInteger)selectedIndex {
	_currentTabIndex = selectedIndex;
	[super setSelectedIndex:selectedIndex];
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	return [[self presenter] getStatusBarStyle:self.resolveOptions];
}

#pragma mark UITabBarControllerDelegate

- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController {
	[self.eventEmitter sendBottomTabSelected:@(tabBarController.selectedIndex) unselected:@(_currentTabIndex)];
	_currentTabIndex = tabBarController.selectedIndex;
}

@end
