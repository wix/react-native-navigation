#import <Foundation/Foundation.h>
#import "RNNReactButtonView.h"
#import "RNNReactTitleView.h"
#import "RNNComponentOptions.h"
#import "RNNComponentViewCreator.h"

@interface RNNReactComponentRegistry : NSObject

- (instancetype)initWithCreator:(id<RNNComponentViewCreator>)creator;

- (RNNReactButtonView *)createComponentIfNotExists:(RNNComponentOptions *)component parentComponentId:(NSString *)parentComponentId componentType:(RNNComponentType)componentType reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock;

- (void)removeComponent:(NSString *)componentId;

- (void)removeChildComponent:(NSString *)childId;

- (void)clearComponentsForParentId:(NSString *)parentComponentId;

- (void)clear;

@end
