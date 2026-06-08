#import "RNNScreenTransition.h"
#import "BoolParser.h"
#import "OptionsArrayParser.h"
#import "RNNElementFinder.h"
#import "RNNLayoutProtocol.h"
#import "RNNUtils.h"
#import "TextParser.h"
#import "UIViewController+LayoutProtocol.h"
#import <UIKit/UIKit.h>

@implementation RNNScreenTransition

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.topBar = [[ElementTransitionOptions alloc] initWithDict:dict[@"topBar"]];
    self.content = [[RNNEnterExitAnimation alloc] initWithDict:dict[@"content"]];
    self.bottomTabs = [[ElementTransitionOptions alloc] initWithDict:dict[@"bottomTabs"]];
    self.enable = [BoolParser parse:dict key:@"enabled"];
    self.waitForRender = [BoolParser parse:dict key:@"waitForRender"];
    self.duration = [TimeIntervalParser parse:dict key:@"duration"];
    self.sharedElementTransitions = [OptionsArrayParser parse:dict
                                                          key:@"sharedElementTransitions"
                                                      ofClass:SharedElementTransitionOptions.class];
    self.elementTransitions = [OptionsArrayParser parse:dict
                                                    key:@"elementTransitions"
                                                ofClass:ElementTransitionOptions.class];
    NSDictionary *zoom = dict[@"zoom"];
    if ([zoom isKindOfClass:[NSDictionary class]]) {
        self.zoomFromId = [TextParser parse:zoom key:@"fromId"];
        self.zoomEnabled = [BoolParser parse:zoom key:@"enabled"];
    }

    return self;
}

- (void)mergeOptions:(RNNScreenTransition *)options {
    [self.topBar mergeOptions:options.topBar];
    [self.content mergeOptions:options.content];
    [self.bottomTabs mergeOptions:options.bottomTabs];

    if (options.enable.hasValue)
        self.enable = options.enable;
    if (options.waitForRender.hasValue)
        self.waitForRender = options.waitForRender;
    if (options.duration.hasValue)
        self.duration = options.duration;
    if (options.sharedElementTransitions)
        self.sharedElementTransitions = options.sharedElementTransitions;
    if (options.elementTransitions)
        self.elementTransitions = options.elementTransitions;
    if (options.zoomFromId.hasValue)
        self.zoomFromId = options.zoomFromId;
    if (options.zoomEnabled.hasValue)
        self.zoomEnabled = options.zoomEnabled;
}

- (BOOL)hasCustomAnimation {
    return (self.topBar.hasAnimation || self.content.hasAnimation || self.bottomTabs.hasAnimation ||
            self.sharedElementTransitions || self.elementTransitions);
}

- (BOOL)hasZoomTransition {
    if (self.hasCustomAnimation) {
        return NO;
    }

    NSString *fromId = [self.zoomFromId withDefault:@""];
    return [self.zoomEnabled withDefault:YES] && fromId.length > 0;
}

- (BOOL)shouldWaitForRender {
	return [self.waitForRender withDefault: [RNNUtils getDefaultWaitForRender]] || self.hasCustomAnimation;
}

- (void)applyZoomToViewController:(UIViewController *)destination
         fromSourceViewController:(UIViewController *)source {
    if (![self hasZoomTransition]) {
        return;
    }

    if (@available(iOS 18.0, *)) {
        NSString *fromId = [[self.zoomFromId withDefault:@""] copy];
        destination.preferredTransition = [UIViewControllerTransition
            zoomWithOptions:nil
            sourceViewProvider:^UIView *(UIZoomTransitionSourceViewProviderContext *context) {
              UIViewController *sourceVC = context.sourceViewController ?: source;
              if (![sourceVC conformsToProtocol:@protocol(RNNLayoutProtocol)]) {
                  return nil;
              }

              UIViewController<RNNLayoutProtocol> *rnnSourceVC =
                  (UIViewController<RNNLayoutProtocol> *)sourceVC;
              UIView *reactView = rnnSourceVC.presentedComponentViewController.reactView;
              if (reactView == nil) {
                  return nil;
              }

              return [RNNElementFinder findElementForId:fromId inView:reactView];
            }];
    }
}

- (NSTimeInterval)maxDuration {
    NSTimeInterval maxDuration = 0;
    if ([self.topBar maxDuration] > maxDuration) {
        maxDuration = [self.topBar maxDuration];
    }

    if ([self.content maxDuration] > maxDuration) {
        maxDuration = [self.content maxDuration];
    }

    if ([self.bottomTabs maxDuration] > maxDuration) {
        maxDuration = [self.bottomTabs maxDuration];
    }

    for (ElementTransitionOptions *elementTransition in self.elementTransitions) {
        if (elementTransition.maxDuration > maxDuration) {
            maxDuration = elementTransition.maxDuration;
        }
    }

    for (SharedElementTransitionOptions *sharedElementTransition in self.sharedElementTransitions) {
        if (sharedElementTransition.maxDuration > maxDuration) {
            maxDuration = sharedElementTransition.maxDuration;
        }
    }

    return maxDuration;
}

@end
