#import <Foundation/Foundation.h>
#import "RNNReactView.h"
#import "RNNComponentOptions.h"
#import "RNNRootViewCreator.h"

@interface RNNReactComponentRegistry : NSObject

- (instancetype)initWithCreator:(id<RNNRootViewCreator>)creator;

- (RNNReactView *)createComponentIfNotExists:(RNNComponentOptions *)component parentComponentId:(NSString *)parentComponentId reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock;

- (void)removeComponent:(NSString *)componentId;

- (void)clearComponentsForParentId:(NSString *)parentComponentId;

- (void)clear;

@end
