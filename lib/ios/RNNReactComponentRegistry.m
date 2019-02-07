#import "RNNReactComponentRegistry.h"

@interface RNNReactComponentRegistry () {
	id<RNNRootViewCreator> _creator;
	NSMutableDictionary* _componentStore;
}

@end

@implementation RNNReactComponentRegistry

- (instancetype)initWithCreator:(id<RNNRootViewCreator>)creator {
	self = [super init];
	_creator = creator;
	_componentStore = [NSMutableDictionary new];
	return self;
}

- (RNNReactView *)createComponentIfNotExists:(RNNComponentOptions *)component parentComponentId:(NSString *)parentComponentId reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
	NSMutableDictionary* parentComponentDict = [self componentsForParentId:parentComponentId];
	
	RNNReactView* reactView = [parentComponentDict objectForKey:component.componentId.get];
	if (!reactView) {
		reactView = (RNNReactView *)[_creator createRootViewFromComponentOptions:component reactViewReadyBlock:reactViewReadyBlock];
		[parentComponentDict setObject:reactView forKey:component.componentId.get];
	} else if (reactViewReadyBlock) {
		reactViewReadyBlock();
	}
	
	return reactView;
}

- (NSMutableDictionary *)componentsForParentId:(NSString *)parentComponentId {
	if (!_componentStore[parentComponentId]) {
		[_componentStore setObject:[NSMutableDictionary new] forKey:parentComponentId];;
	}
	
	return [_componentStore objectForKey:parentComponentId];;
}

- (void)removeComponent:(NSString *)componentId {
	if ([_componentStore objectForKey:componentId]) {
		[_componentStore removeObjectForKey:componentId];
	}
}

- (void)clean {
	[_componentStore removeAllObjects];
}


@end
