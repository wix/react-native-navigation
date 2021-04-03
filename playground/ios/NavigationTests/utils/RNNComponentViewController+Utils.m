#import "RNNComponentViewController+Utils.h"
#import "RNNTestRootViewCreator.h"

@implementation RNNComponentViewController (Utils)

+ (RNNComponentViewController *)createWithComponentId:(NSString *)componentId
                                       initialOptions:(RNNNavigationOptions *)initialOptions {
    RNNLayoutInfo *layoutInfo = [[RNNLayoutInfo alloc] init];
    layoutInfo.componentId = componentId;
    RNNButtonsPresenter *buttonsPresenter =
        [[RNNButtonsPresenter alloc] initWithComponentRegistry:nil eventEmitter:nil];

    RNNComponentViewController *component = [[RNNComponentViewController alloc]
        initWithLayoutInfo:layoutInfo
           rootViewCreator:[[RNNTestRootViewCreator alloc] init]
              eventEmitter:nil
                 presenter:[[RNNComponentPresenter alloc]
                               initWithComponentRegistry:nil
                                          defaultOptions:nil
                                        buttonsPresenter:buttonsPresenter]
                   options:initialOptions
            defaultOptions:nil];
    [buttonsPresenter bindViewController:component];
    return component;
}

+ (RNNComponentViewController *)createWithComponentId:(NSString *)componentId {
    return [self createWithComponentId:componentId
                        initialOptions:RNNNavigationOptions.emptyOptions];
}

@end
