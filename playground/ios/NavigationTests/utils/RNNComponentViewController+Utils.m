#import "RNNComponentViewController+Utils.h"
#import "RNNTestRootViewCreator.h"

@implementation RNNComponentViewController (Utils)

+ (RNNComponentViewController *)createWithComponentId:(NSString *)componentId
                                       initialOptions:(RNNNavigationOptions *)initialOptions {
    RNNLayoutInfo *layoutInfo = [[RNNLayoutInfo alloc] init];
    layoutInfo.componentId = componentId;
    return [[RNNComponentViewController alloc]
        initWithLayoutInfo:layoutInfo
           rootViewCreator:[[RNNTestRootViewCreator alloc] init]
              eventEmitter:nil
                 presenter:[[RNNComponentPresenter alloc] initWithComponentRegistry:nil
                                                                     defaultOptions:nil]
                   options:initialOptions
            defaultOptions:nil];
}

+ (RNNComponentViewController *)createWithComponentId:(NSString *)componentId {
    return [self createWithComponentId:componentId
                        initialOptions:RNNNavigationOptions.emptyOptions];
}

@end
