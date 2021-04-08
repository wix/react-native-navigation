#import "BottomTabPresenterCreator.h"
#import "BottomTabsPresenterCreator.h"
#import "RNNBottomTabsController+Helpers.h"
#import <OCMock/OCMock.h>

@implementation RNNBottomTabsController (Helpers)

+ (RNNBottomTabsController *)create {
    return [self createWithChildren:nil];
}

+ (RNNBottomTabsController *)createWithChildren:(NSArray *)children {
    return [self createWithChildren:children options:[RNNNavigationOptions emptyOptions]];
}

+ (RNNBottomTabsController *)createWithChildren:(NSArray *)children
                                        options:(RNNNavigationOptions *)options {
    RNNNavigationOptions *defaultOptions = [RNNNavigationOptions emptyOptions];
    return [[RNNBottomTabsController alloc]
           initWithLayoutInfo:nil
                      creator:nil
                      options:options
               defaultOptions:defaultOptions
                    presenter:[BottomTabsPresenterCreator createWithDefaultOptions:defaultOptions]
           bottomTabPresenter:[BottomTabPresenterCreator createWithDefaultOptions:defaultOptions]
        dotIndicatorPresenter:[[RNNDotIndicatorPresenter alloc]
                                  initWithDefaultOptions:defaultOptions]
                 eventEmitter:[OCMockObject partialMockForObject:RNNEventEmitter.new]
         childViewControllers:children
           bottomTabsAttacher:nil];
}

@end
